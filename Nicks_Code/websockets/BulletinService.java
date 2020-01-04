package com.database.websockets;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * BulletinService. Service class which implements bulletin repository.
 * 
 * @author Nickolas Mitchell
 */
@Service
public class BulletinService {
	@Autowired
	private BulletinRepository bulletinRepository;

	/**
	 * Returns everything within the bulletin.
	 * 
	 * @return
	 */
	public List<Bulletin> getBulletin() {
		return bulletinRepository.findAll();
	}

	/**
	 * Addes data from the websocket to the bulletin.
	 * 
	 * @param bulletin
	 * @return
	 */
	public Bulletin addBulletin(Bulletin bulletin) {
		bulletinRepository.save(bulletin);
		return bulletin;
	}

	/**
	 * Returns number of data points within the bulletin.
	 * 
	 * @return
	 */
	public Long count() {
		return bulletinRepository.count();
	}

	/**
	 * Deletes data from the bulletin.
	 * 
	 * @param bulletinId
	 */
	public void deleteById(Long bulletinId) {
		bulletinRepository.deleteById(bulletinId);
	}
}
