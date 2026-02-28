import { axiosInstance } from "./index";

export const makePayment = async(values) =>{
    try{
        console.log("makePayment action in calls --- values -- ",values);
        const response = await axiosInstance.post("/bookings/make-payment", values);
        console.log("makePayment action in calls --- response -- ",response);
        return response.data;
    }catch(err){
        console.error(err);
        
        return err;
    }
}


export const bookShow = async(values) =>{
    try{
        const response = await axiosInstance.post("/bookings/book-show", values);
        console.log("after calling bookshow response ::: ",response);
        return response.data;
    }catch(err){
        console.error(err);
        return err;
    }
}

export const getAllBookings = async() =>{
    try{
        const response = await axiosInstance.get("/bookings/all-bookings");
        return response.data;
    }catch(err){
        console.error(err);
        return err;
    }
}