import React, { useEffect, useState } from "react";
import { GetCurrentUser } from "../calls/users";
import { useNavigate } from "react-router-dom";
import { message, Layout, Menu } from "antd";
import { useDispatch, useSelector } from "react-redux";
import { hideLoading, showLoading } from "../redux/loaderSlice";
import { Header } from "antd/es/layout/layout";
import {
  HomeOutlined,
  LogoutOutlined,
  ProfileOutlined,
  UserOutlined,
} from "@ant-design/icons";
import { Link } from "react-router-dom";
import { setUser } from "../redux/userSlice";
import { max } from "moment";

function ProtectedRoute({ children }) {
  const { user } = useSelector((store) => store.users);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const navItems = [
    {
      label: (
        <span
          onClick={() => {
            navigate("/");
          }}
        >
          Home
        </span>
      ),
      icon: <HomeOutlined />,
    },

    {
      label:`${user ? user.name : ""}`,
      icon: <UserOutlined />,
      children: [
        {
          label: (
            <span
            onClick={() => {
              console.log("user role ",user.name,user.id);
              if (user.role === 'ADMIN') {
                navigate("/admin");
              } else if (user.role === 'PARTNER') {
                navigate("/partner");
              } else {
                navigate("/profile");
              }
            }}
            >
{user?.role === 'ADMIN' ? "Admin Panel" : user?.role === 'PARTNER' ? "Partner" : "My Profile"}
            </span>
          ),
          icon: <ProfileOutlined />,
        },
        {
          label: (
            <Link
              to="/login"
              onClick={() => {
                // localStorage.removeItem("token");
                localStorage.clear();
              }}
            >
              Log Out
            </Link>
          ),
          icon: <LogoutOutlined />,
        },
      ],
    },
  ];

  const getValidUser = async () => {
    try {
      // dispatch(showLoading());
      // console.log("localstorage before check", localStorage.getItem("token"));
      const response = await GetCurrentUser();
      // console.log("response data in getCurrentUser",response);
      
        dispatch(setUser(response.data));
        // console.log("user data is"+response);
        // console.log("user role is"+JSON.stringify(response.data));
        localStorage.setItem("userRole", JSON.stringify(response.data.role));
        dispatch(hideLoading());
      // Hide Loader
    } catch (error) {
      console.log("Error in getValidUser:", error);

      if(error.response?.status==401) {
        localStorage.removeItem("token");
        
        dispatch(setUser(null));
        console.log("Token expired (401) redireecting to login");
        navigate("/login");
      }
      dispatch(setUser(null));  
      message.error(error.message);
    }
  };

  useEffect(() => {
    if (localStorage.getItem("token")) {
      if (!user) {
        // Only fetch if user is not in Redux store
        getValidUser();
      }
    } else {
      navigate("/login");
    }
  }, [user]); // Add user as dependency

  return (
    user && (
      <>
        <Layout>
          <Header
            className="d-flex justify-content-between"
            style={{
              position: "sticky",
              top: 0,
              zIndex: 1,
              width: "100%",
              display: "flex",
              alignItems: "center",
              fontSize: "18px",
              justifyContent: "space-between",
            }}
          >
            <h3 className="demo-logo text-white m-0" style={{ color: "white" }}>
              {/* <HomeOutlined style={{ marginRight: "10px" }} /> */}
              Book My Show
            </h3>
            <Menu theme="dark" mode="horizontal" items={navItems} />
          </Header>
          <div style={{ padding: 24, minHeight: 380, background: "#fff" }}>
            {children}
          </div>
        </Layout>
      </>
    )
  );
}

export default ProtectedRoute;
