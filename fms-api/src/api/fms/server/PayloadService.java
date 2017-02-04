package api.fms.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.services.dynamodbv2.document.Item;

import api.fms.dao.PayloadDao;
import api.fms.dto.PayloadDto;
import api.fms.vo.PayloadVo;
import fms.common.Util.Util;

public class PayloadService {

	/**
	 * 依照 tableName ＆ PayloadVo 建立 Item ，PayloadVoh0 參數不可為 null
	 * 
	 * @param tableName
	 * @param payloadVo
	 * @throws InterruptedException
	 * @throws NullPointerException
	 */
	public void insert(String tableName, PayloadVo payloadVo) throws InterruptedException, NullPointerException {
		PayloadDao.getInstance().putItemBytableNameAndPayloaVo(tableName, payloadVo);
	}

	/**
	 * 依照 PackId 查詢轉 PayloadDto
	 * 
	 * @param tableName
	 * @param packId
	 * @return
	 * @throws Exception 
	 */
	public PayloadDto findByPackId(String tableName, String packId) throws Exception {
		Item item = PayloadDao.getInstance().findItemByPackId(tableName, packId);
		PayloadDto dto = setPayloadDtoByItem(item);
		return dto;
	}

	/**
	 * 查找今天资料
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception 
	 */
	public List<PayloadDto> findPayloadDtoListByToday(String tableName) throws Exception {
		Iterator<Item> iterator = PayloadDao.getInstance().findItemIteratorByTimeRange(tableName, Util.getStartTimeOfDay(), Util.getEndTimeOfDay());
		List<PayloadDto> payloadDtoList = new ArrayList<PayloadDto>();
		payloadDtoList.addAll(setPayloadDtoListByItemIterator(iterator));

		return payloadDtoList;
	}

	/**
	 * 依照 tableName & packId 刪除 Item
	 * 
	 * @param tableName
	 * @param packId
	 */
	public void deleteItemByPackId(String tableName, String packId) {
		PayloadDao.getInstance().deleteItemByPackId(tableName, packId);
	}
	
	/**
	 * 依照 Item 轉JSON 透過 Jackson 轉 PayloadDto
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public PayloadDto setPayloadDtoByItem(Item item) throws Exception {
		System.out.println(item.toJSON());
		PayloadDto dto = null;
		dto = Util.readValue(item.toJSON(), PayloadDto.class);
		return dto;
	}
	
	/**
	 * 依照 Item Iterator 组 List<PayloadDto>
	 * 
	 * @param iterator
	 * @return
	 * @throws Exception 
	 */
	private List<PayloadDto> setPayloadDtoListByItemIterator(Iterator<Item> iterator) throws Exception {
		List<PayloadDto> payloadDtoList = new ArrayList<PayloadDto>();
		while (iterator.hasNext()) {
			Item item = iterator.next();
			PayloadDto dto = setPayloadDtoByItem(item);
			payloadDtoList.add(dto);
		}
		return payloadDtoList;
	}
}
