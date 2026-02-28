import React from 'react'
import { useEffect, useState } from 'react';
import { Row, Col, Input, message} from "antd";
import { SearchOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import {useDispatch} from "react-redux";
import { hideLoading, showLoading } from "../../redux/loaderSlice";
import { getAllMovies } from '../../calls/movies';
import moment from "moment";
function Home() {
  const [movies, setMovies] = useState(null);
  const [ searchText, setSearchText] = useState("");
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const getData = async() => {
    try{
      dispatch(showLoading());
      const response = await getAllMovies();
      console.log("after getALlmovies"+response.data);
      
      if(response.success){
        setMovies(response.data);
      }else{
        message.error(response.message);
      }
      dispatch(hideLoading()) ;
    }catch(err){
      message.error(err.message);
      dispatch(hideLoading()) 
    }
  }

  const handleSearch = (e) => {
    setSearchText(e.target.value);
  }

  useEffect(() => {
    getData();
  }, [] );

  return (
    <>
      <Row className="justify-content-center w-100">
        <Col xs={{ span: 24 }} lg={{ span: 12 }}>
          <Input
            placeholder="Type here to search for movies"
            onChange={handleSearch}
            prefix={<SearchOutlined />}
          />
          <br />
          <br />
          <br />
        </Col>
      </Row>
      <Row
        className="justify-content-center"
        gutter={{
          xs: 8,
          sm: 16,
          md: 24,
          lg: 32,
        }}
      >
        {movies &&
          movies
            .filter((movie) =>
              movie.name.toLowerCase().includes(searchText.toLowerCase())
            )
            .map((movie) => (
              <Col
                className="gutter-row mb-5"
                key={movie._id}
                span={{
                  xs: 24,
                  sm: 24,
                  md: 12,
                  lg: 10,
                }}
              >
                <div className="text-center">
                  <img
                    onClick={() => {
                      navigate(
                        `/movie/${movie._id}?date=${moment().format(
                          "YYYY-MM-DD"
                        )}`
                      );
                    }}
                    className="cursor-pointer"
                    src={movie.poster}
                    alt="Movie Poster"
                    width={200}
                    style={{ borderRadius: "8px" }}
                  />
                  <h3
                    onClick={() => {
                      navigate(
                        `/movie/${movie._id}?date=${moment().format(
                          "YYYY-MM-DD"
                        )}`
                      );
                    }}
                    className="cursor-pointer"
                  >
                    {movie.name}
                  </h3>
                </div>
              </Col>
            ))}
      </Row>
    </>
  );
}

export default Home
