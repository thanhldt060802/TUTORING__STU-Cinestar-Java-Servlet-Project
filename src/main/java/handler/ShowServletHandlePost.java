package handler;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ShowDAO;
import model.Show;

public class ShowServletHandlePost {
	
	private ShowDAO showDAO;
	
	public ShowServletHandlePost() {
		this.showDAO = new ShowDAO();
	}
	
	public void handleCreateShow(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long movieId = Long.parseLong(request.getParameter("movieIdInput"));
		Long theaterId = Long.parseLong(request.getParameter("theaterIdInput"));
		Timestamp startAt = null;
		try {
			startAt = new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("startAtInput")).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Long price = Long.parseLong(request.getParameter("priceInput"));
		Integer discountPercentage = Integer.parseInt(request.getParameter("discountPercentageInput"));
		
		Show newShow = new Show();
		newShow.setShowId(System.nanoTime());
		newShow.setMovieId(movieId);
		newShow.setTheaterId(theaterId);
		newShow.setStartAt(startAt);
		newShow.setPrice(price);
		newShow.setDiscountPercentage(discountPercentage);
		if(!this.showDAO.createShow(newShow, null)) {
			System.out.println("Create show failed");
			response.sendRedirect("./getShows");
			return;
		}
		
		System.out.println("Create show successful");
		response.sendRedirect("./getShows");
	}
	
	public void handleUpdateShow(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long id = Long.parseLong(request.getParameter("idInput"));
		Timestamp startAt = null;
		try {
			startAt = new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("startAtInput")).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Long price = Long.parseLong(request.getParameter("priceInput"));
		Integer discountPercentage = Integer.parseInt(request.getParameter("discountPercentageInput"));
		
		Show foundShow = this.showDAO.getShowById(id);
		foundShow.setStartAt(startAt);
		foundShow.setPrice(price);
		foundShow.setDiscountPercentage(discountPercentage);
		
		if(!this.showDAO.updateShow(foundShow, null)) {
			System.out.println("Update show failed");
			response.sendRedirect("./getShowDetail?id=" + id);
			return;
		}
		
		System.out.println("Update show successful");
		response.sendRedirect("./getShowDetail?id=" + id);
	}

}
