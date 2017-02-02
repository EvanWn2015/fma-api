package api.fms.test;

import java.util.ArrayList;

import api.fms.dao.PayloadDao;
import api.fms.dto.PayloadDto;
import api.fms.vo.PayloadVo;

public class Test {

	public static void main(String[] args) {

//		try {
//			DNDBService.getInstance().createTable("testTable");
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
	
		PayloadVo payloadVo = new PayloadVo("22");
		try {
			PayloadDao.getInstance().insert("testTable", payloadVo);
		} catch (NullPointerException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
// 		
//		
//		PayloadDto dto =DNDBService.getInstance().findByPackId("testTable", "22");
		

		
//		// get count
//		int ocunt = DNDBService.getInstance().getTablesCount();
//		System.out.println(ocunt + "");
//		
//		
//		try {
//			DNDBService.getInstance().deleteTable("testTable");
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
