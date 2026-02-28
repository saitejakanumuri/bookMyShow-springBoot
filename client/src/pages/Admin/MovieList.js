import React, { useEffect, useState } from "react";
import { Button, Table } from "antd";
import { getAllMovies } from "../../calls/movies";
import { showLoading, hideLoading } from "../../redux/loaderSlice";
import { useDispatch } from "react-redux";
import moment from "moment";
import { EditOutlined, DeleteOutlined } from "@ant-design/icons";
import MovieForm from "./MovieForm";
import DeleteMovieModal from "./DeleteMovieModal";


function MovieList(){
    const [movies,setMovies] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedMovie, setSelectedMovie] = useState(null);
    const [formType, setFormType] = useState("add");
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const dispatch = useDispatch();
    const getData = async () => {
        dispatch(showLoading());
        const response = await getAllMovies();
        const allMovies = response.data;
        setMovies(
            allMovies.map((item)=>{
                return {...item,key:`movie${item._id}`};
            })
        );
        dispatch(hideLoading());
    };

    const tableHeadings  = [
        {
            title:"Poster",
            dataIndex:"poster",
            render:(text,data) =>{
                return (
                    <img
                    width="75"
                    height="115"
                    style={{ objectFit: "cover" }}
                    src={data.poster}
                    alt="movie poster"
                />
                );
            },
        },
         {
      title: "Movie Name",
      dataIndex: "name",
    },
    {
      title: "Description",
      dataIndex: "description",
    },
    {
      title: "Duration",
      dataIndex: "duration",
      render: (text) => {
        return `${text} Min`;
      },
    },
    {
      title: "Genre",
      dataIndex: "genre",
    },
    {
      title: "Language",
      dataIndex: "language",
    },
    {
      title: "Release Date",
      dataIndex: "releaseDate",
      render: (text, data) => {
        return moment(data.releaseDate).format("MM-DD-YYYY");
      },
    },
    {
      title: "Action",
      render: (text, data) => {
        return (
          <div>
            <Button
              onClick={() => {
                setIsModalOpen(true);
                setSelectedMovie(data);
                setFormType("edit");
              }}
            >
              <EditOutlined />
            </Button>
            <Button
              onClick={() => {
                setIsDeleteModalOpen(true);
                setSelectedMovie(data);
              }}
            >
              <DeleteOutlined />
            </Button>
          </div>
        );
      },
    }
    ];

    useEffect(() => {
        getData();
    }, []); 

    return (
        <>
        <div className="d-flex justify-content-end">
            <Button
              onClick={() => {
                setIsModalOpen(true);
                setFormType("add");
              }}
            >
              Add Movie
            </Button>
        </div>
        <Table dataSource={movies} columns={tableHeadings}/>
        {isModalOpen && (
            <MovieForm
             isModalOpen = {isModalOpen}
             setIsModalOpen = {setIsModalOpen}
             selectedMovie = {selectedMovie}
             setSelectedMovie = {setSelectedMovie}
             formType = {formType}
             getData = {getData}
            />
        )}

        {
            isDeleteModalOpen && (
            <DeleteMovieModal
                isDeleteModalOpen={isDeleteModalOpen}
                setIsDeleteModalOpen={setIsDeleteModalOpen}
                selectedMovie = {selectedMovie}
                setSelectedMovie={setSelectedMovie}
                getData={getData}
            />
            )
        }



        </>
    )
}

export default MovieList;