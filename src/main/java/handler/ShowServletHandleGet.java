package handler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MovieDAO;
import dao.ProductDAO;
import dao.SeatDAO;
import dao.ShowDAO;
import dao.TheaterDAO;
import model.Movie;
import model.Product;
import model.Seat;
import model.Show;
import model.Theater;
import model.User;

public class ShowServletHandleGet {
	
	private ShowDAO showDAO;
	private MovieDAO movieDAO;
	private TheaterDAO theaterDAO;
	private SeatDAO seatDAO;
	private ProductDAO productDAO;
	
	public ShowServletHandleGet() {
		this.showDAO = new ShowDAO();
		this.movieDAO = new MovieDAO();
		this.theaterDAO = new TheaterDAO();
		this.seatDAO = new SeatDAO();
		this.productDAO = new ProductDAO();
	}
	
	public void handleGetShows(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Show> showList = this.showDAO.getShows();
		request.setAttribute("showList", showList);
		

		Map<Long, String> showIdMovieNameMap = showList.stream()
				.collect(Collectors.toMap((show) -> show.getShowId(), (show) -> this.movieDAO.getMovieById(show.getMovieId()).getTitle()));
		request.setAttribute("showIdMovieNameMap", showIdMovieNameMap);
		
		Map<Long, String> showIdTheaterNameMap = showList.stream()
				.collect(Collectors.toMap((show) -> show.getShowId(), (show) -> this.theaterDAO.getTheaterById(show.getTheaterId()).getName()));
		request.setAttribute("showIdTheaterNameMap", showIdTheaterNameMap);
		
		List<Movie> movieList = this.movieDAO.getMovies();
		request.setAttribute("movieList", movieList);
		
		List<Theater> theaterList = this.theaterDAO.getTheaters();
		request.setAttribute("theaterList", theaterList);
		
		request.getRequestDispatcher("./table-show.jsp").forward(request, response);
	}
	
	public void handleGetShowDetail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long id = Long.parseLong(request.getParameter("id"));
		
		Show foundShow = this.showDAO.getShowById(id);
		request.setAttribute("foundShow", foundShow);
		
		Movie foundMovie = this.movieDAO.getMovieById(foundShow.getMovieId());
		request.setAttribute("foundMovie", foundMovie);
		
		Theater foundTheater = this.theaterDAO.getTheaterById(foundShow.getTheaterId());
		request.setAttribute("foundTheater", foundTheater);
		
		List<Seat> seatList = this.seatDAO.getSeatsByShowId(id);
		request.setAttribute("seatList", seatList);

		User loginUser = (User)request.getSession().getAttribute("loginUser");
		
		if(loginUser != null && loginUser.getRoleName().equals("ADMIN")) {
			request.getRequestDispatcher("./modify-show.jsp").forward(request, response);
		}else {
			List<Product> productList = this.productDAO.getProducts();
			request.setAttribute("productList", productList);
			request.getRequestDispatcher("./show-detail.jsp").forward(request, response);
		}
	}
	
	public void handleCreateShow(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("./getShows");
	}
	
	public void handleUpdateShow(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("./getShows");
	}
	
	public void handleDeleteShow(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long id = Long.parseLong(request.getParameter("id"));
		
		if(!this.showDAO.deleteShow(id, null)) {
			System.out.println("Delete show failed");
			response.sendRedirect("./getShows");
			return;
		}
		
		System.out.println("Delete user successful");
		response.sendRedirect("./getShows");
	}
	
}
