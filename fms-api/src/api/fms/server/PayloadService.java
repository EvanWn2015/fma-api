package api.fms.server;

import api.fms.dao.PayloadDao;
import api.fms.dto.PayloadDto;
import api.fms.vo.PayloadVo;

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
		PayloadDao.getInstance().insert(tableName, payloadVo);	
	}
	
	/**
	 * 依照 PackId 查詢轉 PayloadDto
	 * 
	 * @param tableName
	 * @param packId
	 * @return
	 */
	public PayloadDto findByPackId(String tableName, String packId) {
		PayloadDto dto = PayloadDao.getInstance().findByPackId(tableName, packId);
		return dto;
	}
	
	/**
	 * 依照 tableName & packId 刪除 Item
	 * @param tableName
	 * @param packId
	 */
	public void deleteItemByPackId (String tableName, String packId){
		PayloadDao.getInstance().deleteItemByPackId(tableName, packId);
	}
}
