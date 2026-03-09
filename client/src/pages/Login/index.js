import React, { useEffect } from "react";
import { Button, Form, Input, message } from "antd";
import { Link, useNavigate } from "react-router-dom";
import { LoginUser } from "../../calls/users";
import "./Login.css";

function Login() {
  const navigate = useNavigate();
  const [form] = Form.useForm();

  const onFinish = async (values) => {
    try {
      const response = await LoginUser(values);
      console.log("resp::: ", response);
      if (response.success) {
        message.success(response.message);
        localStorage.setItem("token", response.data.token);
        localStorage.setItem("userRole", JSON.stringify(response.data.role));
        navigate("/");
      } else {
        message.error(response.message);
      }
    } catch (err) {
      message.error(err.message);
    }
  };

  useEffect(() => {
    if (localStorage.getItem("token")) {
      navigate("/");
    }
  }, [navigate]);

  return (
    <div className="login-container">
      <div className="login-background"></div>

      <div className="login-header">
        <h2>BookMyShow</h2>
        <p className="login-tagline">Your Entertainment Destination</p>
      </div>

      <div className="login-promo">
        <h3>Book Your Next Experience</h3>
        <p>Discover movies, shows, and events happening near you</p>
      </div>

      <div className="login-form-wrapper">
        <h1 className="login-title">Sign In</h1>

        <Form
          form={form}
          layout="vertical"
          onFinish={onFinish}
          className="login-form"
        >
          <Form.Item
            label="Email"
            htmlFor="email"
            name="email"
            rules={[
              { required: true, message: "Email is required!" },
              {
                type: "email",
                message: "Please enter a valid email!",
              },
            ]}
          >
            <Input
              id="email"
              type="email"
              placeholder="Enter your Email"
              autoComplete="email"
            />
          </Form.Item>

          <Form.Item
            label="Password"
            name="password"
            htmlFor="password"
            rules={[
              { required: true, message: "Password is required!" },
            ]}
          >
            <Input
              id="password"
              type="password"
              placeholder="Enter your Password"
              autoComplete="current-password"
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              block
              htmlType="submit"
            >
              Sign In
            </Button>
          </Form.Item>
        </Form>

        <div className="login-links">
          <p>
            New User?{" "}
            <Link to="/register">Create an account</Link>
          </p>
          <p>
            Forgot Password?{" "}
            <Link to="/forgot">Reset Password</Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Login;
