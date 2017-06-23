package com.fzh.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fzh.entity.ImageTextMore;
import com.fzh.jdbctemplate.JDBCUtil;

@Repository
public class ImageTextMoreDAO extends JDBCUtil {
    
	public int save(ImageTextMore imageTextMore) {
		return this.insert(imageTextMore);
	}
	
	public int update(ImageTextMore imageTextMore) {
		return this.update(imageTextMore, "more_image_text_no");
	}
	
	public int delete(Long moreImageTextNo) {
		return jdbcTemplate.update("delete from image_text_more where more_image_text_no = ?", new Object[]{ moreImageTextNo });
	}
	
	public ImageTextMore findByPK(String moreImageTextNo) {
		String sql = "select * from image_text_more i where i.more_image_text_no = ?";
		 return this.getObject(ImageTextMore.class, sql, new Object[]{ moreImageTextNo });
	}
	
	public List<ImageTextMore> findImageTextMoreList(Long imageTextNo) {
		String sql = "select * from image_text_more i where i.image_text_no = ?";
    	return this.getListObject(ImageTextMore.class, sql, new Object[]{ imageTextNo });
    }
	
	public List<ImageTextMore> findSubImageText(Long imageTextNo) {
		String sql = "select * from image_text_more i where i.image_text_no = ?";
		return this.getListObject(ImageTextMore.class, sql, new Object[]{ imageTextNo });
    }
	
	public int[] batchUpdate(List<Object[]> values) {
		String sql = "delete from image_text_more where id in ?";
		return batchUpdate(sql, values);
	}
	
}
