 import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { hideLoading, showLoading } from "../redux/loaderSlice";
import { getShowById } from "../calls/shows";
import { useParams, useNavigate } from "react-router-dom";
import { Card, Col, message, Row, Button } from "antd";
import "./BookShow.css";

import moment from "moment";
import { bookShow, makePayment } from "../calls/booking";
import { CardElement, useStripe, useElements } from "@stripe/react-stripe-js";

function BookShow() {
  const params = useParams();
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { user } = useSelector((store) => store.users);
  console.log("Redux user ===", user);
  const [show, setShow] = useState(null);
  const [selectedSeats, setSelectedSeats] = useState([]);

  const stripe = useStripe();
  const elements = useElements();

  const getData = async () => {
    try {
      dispatch(showLoading());
      const response = await getShowById(params.id);
      if (response.success) {
        setShow(response.data);
      } else {
        message.error(response.message);
      }
      dispatch(hideLoading());
    } catch (err) {
      message.error(err.message);
      dispatch(hideLoading());
    }
  };

  useEffect(() => {
    getData();
  }, []);

  const book = async (transactionId) => {
    try {
      const response = await bookShow({
        show: params.id,
        user: user._id,
        seats: selectedSeats,
        transactionId,
      });
      if (response.success) {
        message.success("Show has been booked!");
        navigate("/profile");
      } else {
        message.error(response.message);
      }
      dispatch(hideLoading());
    } catch (err) {
      message.error(err.message);
      dispatch(hideLoading());
    }
  };

  const handlePayment = async () => {
    if (!stripe || !elements) return;
    if (!user || !user.email) {
      console.log("User not loaded. Please log in again.");
      return;
    }

    try {
      dispatch(showLoading());
      console.log("user---- ",user._id," user email ===",user.email);
      const cardElement = elements.getElement(CardElement);
      const { error, paymentMethod } = await stripe.createPaymentMethod({
        type: "card",
        card: cardElement,
        billing_details: { email: user.email },
      });
      console.log("Payment method created:", paymentMethod);
      console.log("Payment method ID:", paymentMethod?.id);

      if (error) {
        console.error("Payment method creation error:", error);
        message.error(error.message);
        dispatch(hideLoading());
        return;
      }

      if (!paymentMethod?.id) {
        message.error("Failed to create payment method ID");
        dispatch(hideLoading());
        return;
      }

      // Call backend with paymentMethodId
      console.log("Sending to backend - paymentMethodId:", paymentMethod.id);
      const response = await makePayment({
        paymentMethodId: paymentMethod.id,
        amount: selectedSeats.length * show.ticketPrice * 100, // amount in paise
      });

      if (response.success) {
        message.success(response.message);
        book(response.data); // transactionId
      } else {
        message.error(response.message);
      }

      dispatch(hideLoading());
    } catch (err) {
      message.error(err.message);
      dispatch(hideLoading());
    }
  };

  const getSeats = () => {
    let columns = 12;
    let totalSeats = 120;
    let rows = totalSeats / columns;

    return (
      <div className="d-flex flex-column align-items-center">
        <div className="w-100 max-width-600 mx-auto mb-25px">
          <p className="text-center mb-10px">
            Screen this side, you will be watching in this direction
          </p>
          <div className="screen-div"></div>
        </div>
        <ul className="seat-ul justify-content-center">
          {Array.from(Array(rows).keys()).map((row) =>
            Array.from(Array(columns).keys()).map((column) => {
              let seatNumber = row * columns + column + 1;
              let seatNumberStr = String(seatNumber);

              let seatClass = "seat-btn";
              if (selectedSeats.includes(seatNumber)) seatClass += " selected";
              if (show.bookedSeats.includes(seatNumberStr)) seatClass += " booked";

              if (seatNumber <= totalSeats)
                return (
                  <li key={seatNumber}>
                    <button
                      className={seatClass}
                      onClick={() => {
                        if (selectedSeats.includes(seatNumber)) {
                          setSelectedSeats(
                            selectedSeats.filter(
                              (curSeatNumber) => curSeatNumber !== seatNumber
                            )
                          );
                        } else {
                          if(!show.bookedSeats.includes(seatNumberStr))
                          setSelectedSeats([...selectedSeats, seatNumber]);
                        }
                      }}
                    >
                      {seatNumber}
                    </button>
                  </li>
                );
            })
          )}
        </ul>

        <div className="d-flex bottom-card justify-content-between w-100 max-width-600 mx-auto mb-25px mt-3">
          <div className="flex-1">
            Selected Seats: <span>{selectedSeats.join(", ")}</span>
          </div>
          <div className="flex-shrink-0 ms-3">
            Total Price:{" "}
            <span>Rs. {selectedSeats.length * show.ticketPrice}</span>
          </div>
        </div>
      </div>
    );
  };

  return (
    <>
      {show && (
        <Row gutter={24}>
          <Col span={24}>
            <Card
              title={
                <div className="movie-title-details">
                  <h1>{show.movie.name}</h1>
                  <p>
                    Theatre: {show.theatre.name}, {show.theatre.address}
                  </p>
                </div>
              }
              extra={
                <div className="show-name py-3">
                  <h3>
                    <span>Show Name:</span> {show.name}
                  </h3>
                  <h3>
                    <span>Date & Time: </span>
                    {moment(show.date).format("MMM Do YYYY")} at{" "}
                    {moment(show.time, "HH:mm").format("hh:mm A")}
                  </h3>
                  <h3>
                    <span>Ticket Price:</span> Rs. {show.ticketPrice}/-
                  </h3>
                  <h3>
                    <span>Total Seats:</span> {show.totalSeats}
                    <span> &nbsp;|&nbsp; Available Seats:</span>{" "}
                    {show.totalSeats - show.bookedSeats.length}
                  </h3>
                </div>
              }
              style={{ width: "100%" }}
            >
              {getSeats()}

              {selectedSeats.length > 0 && (
                <div className="max-width-600 max-height-400 mx-auto mt-1 payment-box">
                <div className="card-element-container">
                <CardElement
                  options={{
                    style: {
                      base: {
                        fontSize: "16px",
                        color: "#424770",
                        letterSpacing: "0.025em",
                        fontFamily: "Source Code Pro, monospace",
                        "::placeholder": { color: "#aab7c4" },
                      },
                      invalid: { color: "#9e2146" },
                    },
                  }}
                />

                </div>
              
                <Button
                  type="primary"
                  shape="round"
                  size="large"
                  block
                  onClick={handlePayment}
                  disabled={!stripe}
                  className="mt-3"
                >
                  Pay Now
                </Button>
              </div>
              
              )}
            </Card>
          </Col>
        </Row>
      )}
    </>
  );
}

export default BookShow;
