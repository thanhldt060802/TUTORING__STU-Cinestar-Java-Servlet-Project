package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import infrastructure.MySQLDB;
import model.Movie;

public class MovieDAO {
	
	public List<Movie> getMovies() {
		List<Movie> movies = new ArrayList<Movie>();
		String sqlGetAllMovies = "SELECT * FROM movies";
		Connection connection = MySQLDB.getConnection();
		try {
			PreparedStatement statementGetAllMovies = connection.prepareStatement(sqlGetAllMovies);
			
			ResultSet rsGetAllMovies = statementGetAllMovies.executeQuery();
			while (rsGetAllMovies.next()) {
				Movie movie = new Movie();
				movie.setMovieId(rsGetAllMovies.getLong("movie_id"));
				movie.setTitle(rsGetAllMovies.getString("title"));
				movie.setImage(rsGetAllMovies.getString("image"));
				movie.setGenre(rsGetAllMovies.getString("genre"));
				movie.setDuration(rsGetAllMovies.getInt("duration"));
				movie.setReleaseDateAt(rsGetAllMovies.getTimestamp("release_date_at"));
				movies.add(movie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
                connection.close();
	        } catch (SQLException closeEx) {
	            closeEx.printStackTrace();
	        }
		}
		return movies;
	}
	
	public Movie getMovieById(Long id) {
		Movie foundMovie = null;
		String sqlGetMovieById = "SELECT * FROM movies WHERE movie_id = ?";
		Connection connection = MySQLDB.getConnection();
		try {
			PreparedStatement statementGetMovieById = connection.prepareStatement(sqlGetMovieById);
			statementGetMovieById.setLong(1, id);
			
			ResultSet rsGetMovieById = statementGetMovieById.executeQuery();
			if (rsGetMovieById.next()) {
				foundMovie = new Movie();
				foundMovie.setMovieId(rsGetMovieById.getLong("movie_id"));
				foundMovie.setTitle(rsGetMovieById.getString("title"));
				foundMovie.setImage(rsGetMovieById.getString("image"));
				foundMovie.setGenre(rsGetMovieById.getString("genre"));
				foundMovie.setDuration(rsGetMovieById.getInt("duration"));
				foundMovie.setReleaseDateAt(rsGetMovieById.getTimestamp("release_date_at"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
                connection.close();
	        } catch (SQLException closeEx) {
	            closeEx.printStackTrace();
	        }
		}
		return foundMovie;
	}
			
	public boolean createMovie(Movie newMovie, Connection refConnection) {
		String sqlInsertMovie = "INSERT INTO movies(title, image, genre, duration, release_date_at) VALUES (?, ?, ?, ?, ?)";
		Connection connection = refConnection;
		if(connection == null) {
			connection = MySQLDB.getConnection();
		}
		try {
			PreparedStatement statementInsertMovie = connection.prepareStatement(sqlInsertMovie);
			statementInsertMovie.setString(1, newMovie.getTitle());
			statementInsertMovie.setString(2, newMovie.getImage());
			statementInsertMovie.setString(3, newMovie.getGenre());
			statementInsertMovie.setInt(4, newMovie.getDuration());
			statementInsertMovie.setTimestamp(5, newMovie.getReleaseDateAt());
			statementInsertMovie.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(refConnection == null) {
				try {
	                connection.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
			}
		}
		return false;
	}
	
	public boolean updateMovie(Movie updatedMovie, Connection refConnection) {
		String sqlUpdateMovie = "UPDATE movies SET title = ?, image = ?, genre = ?, duration = ?, release_date_at = ? WHERE movie_id = ?";
		Connection connection = refConnection;
		if(connection == null) {
			connection = MySQLDB.getConnection();
		}
        try {
            PreparedStatement statementUpdateMovie = connection.prepareStatement(sqlUpdateMovie);
            statementUpdateMovie.setString(1, updatedMovie.getTitle());
            statementUpdateMovie.setString(2, updatedMovie.getImage());
            statementUpdateMovie.setString(3, updatedMovie.getGenre());
            statementUpdateMovie.setInt(4, updatedMovie.getDuration());
            statementUpdateMovie.setTimestamp(5, updatedMovie.getReleaseDateAt());
            statementUpdateMovie.setLong(6, updatedMovie.getMovieId());
            statementUpdateMovie.executeUpdate();
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
			if(refConnection == null) {
				try {
	                connection.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
			}
		}
        return false;
	}
	
	public boolean deleteMovie(Long id, Connection refConnection) {
		String sqlDeleteMovie = "DELETE FROM movies WHERE movie_id = ?";
		Connection connection = refConnection;
		if(connection == null) {
			connection = MySQLDB.getConnection();
		}
        try {        	
        	// Xoá tất cả những dữ liệu liên quan Movie trước khi xoá Movie vì Database liên kết
        	
            PreparedStatement statementDeleteMovie = connection.prepareStatement(sqlDeleteMovie);
            statementDeleteMovie.setLong(1, id);
            statementDeleteMovie.executeUpdate();
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
			if(refConnection == null) {
				try {
	                connection.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
			}
		}
        return false;
	}

}
