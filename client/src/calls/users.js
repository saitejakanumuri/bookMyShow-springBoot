import { axiosInstance } from './index';

export const RegisterUser = async (data) => {
    try {
        const response = await axiosInstance.post('/users/register', data);
        return response.data;
    } catch (error) {
        console.error(error);
        return error;
    }
};



export const LoginUser = async(value) => {
    try{
        const response = await axiosInstance.post("/users/login", value);
       console.log("received data after login", response.data);
        return response.data
    }catch(err){
        console.error(err);
        return err;
    }
}

export const GetCurrentUser = async() => {
    try{
        const response = await axiosInstance.get("/users/get-current-user");
        // console.log("response from getCurrentUser",response);
        return response.data
        
    }catch(err){
        console.error('here error', err);
        throw err;
    }
}

export const ForgetPassword = async() => {
    try{
        const response = await axiosInstance.patch("/users/forgetpassword");
        return response.data
    }catch(err){
        console.error(err);
        return err;
    }
}

export const ResetPassword = async() => {
    try{
        const response = await axiosInstance.patch("/users/resetpassword");
        return response.data
    }catch(err){
        console.error(err);
        return err;
    }
}