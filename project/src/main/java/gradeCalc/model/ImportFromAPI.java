package gradeCalc.model;

import java.net.URL;
import java.security.Security;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.TrustManager;


public class ImportFromAPI {
	
	private String courseID;
	private String resultString;
	
	ImportFromAPI(String CourseID) throws Exception {
		this.courseID = CourseID;
		setResultString(CourseID);
	}
	
	void setResultString(String CourseID) throws Exception {
		
		URL url = new URL("https://grades.no/api/v2/courses/" + CourseID.toUpperCase() + "/");
		Scanner sc = new Scanner(url.openStream());
		StringBuffer sb = new StringBuffer();
		
		while (sc.hasNextLine()) {
			sb.append(sc.nextLine());
		}
		sc.close();
		this.resultString = sb.toString();
		
//		final ExecutorService service = Executors.newSingleThreadExecutor();
//
//        try {
//            final Future<Object> f = service.submit(() -> {
//
//    			URL url = new URL("https://grades.no/api/v2/courses/" + CourseID.toUpperCase() + "/");
//    			Scanner sc = new Scanner(url.openStream());
//    			StringBuffer sb = new StringBuffer();
//    			
//    			while (sc.hasNextLine()) {
//    				sb.append(sc.nextLine());
//    			}
//    			sc.close();
//    			this.resultString = sb.toString();	
//            	
//                return "r";
//            });
//
//            System.out.println(f.get(10, TimeUnit.SECONDS));
//        } catch (final TimeoutException e) {
//        	throw new TimeoutException("Det tar for lang tid å nå internett");
//        } catch (final Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            service.shutdown();
//        }

	}
	
	String getCourseName() {
		if (this.resultString != null) {
			String s1 = this.resultString.substring(this.resultString.indexOf("norwegian_name")+17);
			String s2 = s1.substring(0, s1.indexOf('"'));
			return s2;
		}
		return null;
	}

	double getCredit() {
		if (this.resultString != null) {
			String s1 = this.resultString.substring(this.resultString.indexOf("credit")+8);
			String s2 = s1.substring(0, s1.indexOf(','));
			return Double.parseDouble(s2);
		}
		return 0;
	}
	
	double getAverage() {
		if (this.resultString != null) {
			String s1 = this.resultString.substring(this.resultString.indexOf("average")+9);
			String s2 = s1.substring(0, s1.indexOf(","));
			return Double.parseDouble(s2);
		}
		return 0;
	}
	
	@Override
	public String toString() {
		return "CourseID: " + this.courseID + "\nResultString: " + this.resultString;
	}
	
//	public static void main(String[] args) {
//		try {
//			ImportFromAPI i = new ImportFromAPI("met1001");
//			System.out.println(i.toString());
//		} catch (Exception e) {

//			e.printStackTrace();
//		}
//	}

}
