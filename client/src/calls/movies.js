import { axiosInstance } from "./index";

export const addMovie = async(data) =>{
    try{
        const response = await axiosInstance.post("/movies/", data);
        return response.data;
    }catch(err){
        console.error(err);
        return err;
    }
}

//get all movies
export const getAllMovies = async() =>{
    try{
        const response = await axiosInstance.get("/movies/");
        return response.data;

    }catch(err){
        console.error(err)
        return err;
    }
}

// update a movie
export const updateMovie = async(id , values)=>{
    try{
        const response = await axiosInstance.put(`/movies/${id}`, values);
        return response.data;
    }catch(err){
        console.error(err);
        return err;
    }
}


//delete a movie
export const deleteMovie = async(id) => {
    try{
        const response = await axiosInstance.delete(`/movies/${id}`);
        return response.data
    }catch(err){
        console.error(err);
        return err;
    }
}

//fetch a movie by id
export const movieById = async(id) => {
    try{
        const response = await axiosInstance.get(`/movies/${id}`);
        return response.data
    }catch(err){
        console.error(err);
        return err;
    }
}