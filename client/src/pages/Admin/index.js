import React from 'react';
import {Tabs} from 'antd';
import MovieList from './MovieList';
import TheatresTable from './TheatresTable';
import { useNavigate } from "react-router-dom";
import { useEffect } from 'react';

const tabItems = [
    {
        key:'1',
        label:"Movies",
        children:<MovieList/>
    },
    {
        key:'2',
        label:"Theatres",
        children: <TheatresTable/>,
    },
];

function userRoleSatisfied(){
    const userRole = JSON.parse(localStorage.getItem("userRole"));
    // console.log("user role page", userRole);
    return userRole === 'ADMIN';
}



function Admin(){
    const navigate = useNavigate();
    
    useEffect(() => {
        if (!userRoleSatisfied()) {
            // console.log("user role not satisfied");
            navigate("/");
        }
    }, []);

    return (
        <div>
            <h1>Admin Page</h1> 
            <Tabs items={tabItems}/>
        </div>
    )
}
export default Admin;