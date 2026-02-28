import { axiosInstance } from "./index";

//add show
export const addShow = async(value) => {
    try{
        const response = await axiosInstance.post("/shows/", value);
        return response.data;
    }catch(err){
        console.error(err);
        return err;
    }
}

//update a show
export const updateShow = async(id , values) => {
    try{
        const response = await axiosInstance.put(`/shows/${id}`, values)
        return response.data;
    }catch(err){
        console.error(err);
        return err;
    }
}


//delete a show
export const deleteShow = async(id) => {
    try{
        const response = await axiosInstance.delete(`/shows/${id}`);
        return response.data
    }catch(err){
        console.error(err);
        return err;
    }
}


//get all shows by theatre
export const getAllShowsByTheatre = async(value) => {
    try{
        const response = await axiosInstance.post(`/shows/get-all-shows-by-theatre`, value);
        return response.data
    }catch(err){
        console.error(err);
        return err;
    }
}


//get all theatres by movie
export const getAllTheatresByMovie = async(value) => {
    try{
        const response = await axiosInstance.post(`/shows/get-all-theatres-by-movie`, value);
        return response.data
    }catch(err){
        console.error(err);
        return err;
    }
}


//get shows by id
export const getShowById = async(id) => {
    try{
        const response = await axiosInstance.get(`/shows/${id}`);
        return response.data
    }catch(err){
        console.error(err);
        return err;
    }
}