package com.acer.fms.test;

import java.util.ArrayList;

import com.acer.fms.dto.PayloadDto;
import com.acer.fms.sevice.DNDBService;
import com.acer.fms.vo.PayloadVo;

public class Test {

	public static void main(String[] args) {

		PayloadVo payloadVo = new PayloadVo();
		payloadVo.setPackId(22L);
		try {
			DNDBService.getInstance().insert("testTable1", payloadVo);
		} catch (NullPointerException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ArrayList<PayloadDto> dtos =DNDBService.getInstance().findByPackId("testTable1", 22L);

		DNDBService.getInstance();
		// ArrayList<PayloadDto> dtos =
		int ocunt = DNDBService.getTablesCount();

		System.out.println(ocunt + "");
		

	}

}
