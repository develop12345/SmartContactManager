package com.project.smartcontactmanager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.smartcontactmanager.entities.Contact;
import com.project.smartcontactmanager.entities.User;

import jakarta.transaction.Transactional;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	// writing query to find contact of given user Id
	@Query("from Contact as c where c.user.id=:userId")
	public Page<Contact> findContactByUser(@Param("userId")int userId, Pageable p);
	
	@Modifying
	@Transactional
	@Query(value="delete from Contact c where c.cId =:id")
    public void deleteContactById(@Param("id")Integer id);

	@Query("from Contact as c where c.cId =:cId")
	public Contact findByLongId(@Param("cId")long getcId);

	public List<Contact> findByNameContainingAndUser(String name, User user);

}
