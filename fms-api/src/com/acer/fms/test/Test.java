package com.acer.fms.test;

import java.util.ArrayList;

import com.acer.fms.dto.PayloadDto;
import com.acer.fms.sevice.DNDBService;
import com.acer.fms.vo.PayloadVo;

public class Test {

	public static void main(String[] args) {

		
		
		
//		try {
//			DNDBService.getInstance().createTable("testTable");
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
////		
//		PayloadVo payloadVo = new PayloadVo();
//		payloadVo.setPackId(22L);
//		payloadVo.setTimestamp(1212121212L);
//		try {
//			DNDBService.getInstance().insert("testTable", payloadVo);
//		} catch (NullPointerException | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		
//		ArrayList<PayloadDto> dtos =DNDBService.getInstance().findByPackId("testTable1", 22L);
//
		
//		// get count
//		int ocunt = DNDBService.getInstance().getTablesCount();
//		System.out.println(ocunt + "");
		
		
		// delete
		try {
			DNDBService.getInstance().deleteTable("testTable");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//	
		

	}

}
