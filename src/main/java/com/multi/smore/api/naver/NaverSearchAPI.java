package com.multi.smore.api.naver;
// 네이버 검색 API 예제 - 블로그 검색
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;

import com.multi.smore.news.model.vo.News;

public class NaverSearchAPI {

//	public static List<Product> getProductList(String query, int display, int start) {
//        String clientId = "sR7e9agRVrran0oy1CO1"; //애플리케이션 클라이언트 아이디
//        String clientSecret = "MadPis5VIy"; //애플리케이션 클라이언트 시크릿
////        String apiURL = "https://openapi.naver.com/v1/search/news.json";  // 뉴스 JSON 결과
//        String apiURL = "https://openapi.naver.com/v1/search/shop.json";  // 쇼핑 JSON 결과
//        try {
//        	query = URLEncoder.encode(query, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException("검색어 인코딩 실패",e);
//        }
//        
//        StringBuilder url = new StringBuilder();
//
//        url.append(apiURL);
//        url.append("?query="+query);
//        url.append("&display="+display);
//        url.append("&start="+start);
//
//        Map<String, String> requestHeaders = new HashMap<>();
//        requestHeaders.put("X-Naver-Client-Id", clientId);
//        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
//        String responseBody = get(url.toString(), requestHeaders);
//
//        List<Product> list = null;
//        try {
//			list = NaverSearchAPIParser.parseShop(responseBody);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//        return list;
//    }
	
	public static List<News> getNewsList(String query, int display, int start) {
		String clientId = "0uaUIkS2vtFxjBdmhscg"; //애플리케이션 클라이언트 아이디
		String clientSecret = "W5gZ4rz5b8"; //애플리케이션 클라이언트 시크릿
        String apiURL = "https://openapi.naver.com/v1/search/news.json";  // 뉴스 JSON 결과
		try {
			query = URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("검색어 인코딩 실패",e);
		}
		
		StringBuilder url = new StringBuilder();
		
		url.append(apiURL);
		url.append("?query="+query);
		url.append("&display="+display);
		url.append("&start="+start);
		
		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-Naver-Client-Id", clientId);
		requestHeaders.put("X-Naver-Client-Secret", clientSecret);
		String responseBody = get(url.toString(), requestHeaders);
		
		List<News> list = null;
		try {
			list = NaverSearchAPIParser.parseNews(responseBody);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	
    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }


    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);
        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }
}