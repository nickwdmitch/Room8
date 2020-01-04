package com.database.websockets;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * BulletinRepository. Interface which holds the Jpa Repository for bulletin.
 * 
 * @author Nickolas Mitchell
 */
@Repository
public interface BulletinRepository extends JpaRepository<Bulletin, Long> {

}
