import { axiosInstance } from "./index";


//add theatre
 export const addTheatre = async(data) =>{
    try{
        const response = await axiosInstance.post("/theatres/",data);
        return response.data
    }catch(err){
        console.log(err);
        return err;
    }
 }

 //update theatre
 export const updateTheatre = async(id , values) =>{
    try{
        const response = await axiosInstance.put(`/theatres/${id}`,values);
        return response.data
    }catch(err){
        console.error(err);
        return err;
    }
 }

 
//delete a movie
export const deleteTheatre = async(id) => {
    try{
        const response = await axiosInstance.delete(`/theatres/${id}`);
        return response.data
    }catch(err){
        console.error(err);
        return err;
    }
}

//get all theatres
export const getAllTheatres = async() => {
    try{
        const response = await axiosInstance.get("/theatres/get-all-theatres");
        return response.data
    }catch(err){
        console.error(err);
        return err;
    }
}

//get all theatres of a specific owner
export const getAllTheatresPartnerOwns = async() => {
    try{
        const response = await axiosInstance.get("/theatres/get-all-theatres-by-owner");
        return response.data
    }catch(err){
        console.error(err);
        return err;
    }
}