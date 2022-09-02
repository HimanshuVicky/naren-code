package test;

import com.assignsecurities.app.util.Util;

public class TestMe {
	public static void main(String[] args) {
//		LocalDate now = LocalDate.now().minusDays(1);
//		String url;
//		int dayOfMonth = now.getDayOfMonth();
//		System.out.println("dayOfMonth===>"+dayOfMonth);
//		System.out.println("dayOfMonth+\"\".length()====>"+(dayOfMonth+"".length()));
//		System.out.println("d (dayOfMonth+\"\".length()==1)====>"+ (dayOfMonth+"".length()==1));
//		String dayOfMonthStr = (dayOfMonth<10) ? "0"+dayOfMonth : dayOfMonth+"";
//		System.out.println("dayOfMonthStr===>"+dayOfMonthStr);
		int noOfPages = 6;
		String surePassSign1XCoOrdinate = "10"; 
		String surePassSign1YCoOrdinate = "10";
		
		String pagePossitionString = Util.getPossitionJson(noOfPages, surePassSign1XCoOrdinate, surePassSign1YCoOrdinate);
		System.out.println("pagePossitionString===>"+pagePossitionString);
			
	}

	
}
