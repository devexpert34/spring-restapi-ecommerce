package com.rene.ecommerce.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rene.ecommerce.domain.dto.ranking.ClientRankingDTO;
import com.rene.ecommerce.domain.users.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

	@Transactional
	Client findByEmail(String email);
	
	@Transactional
	Client findByCpf(String cpf);
	
	
	@Modifying
	@Query(value="select id, name, numberOfBuys,  howMuchMoneyThisClientHasSpent from tb_clients order by howMuchMoneyThisClientHasSpent DESC limit 10 ",nativeQuery = true)
	List<ClientRankingDTO> returnRankingClient();
}
