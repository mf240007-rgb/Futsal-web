package com.tugas.web.repository;

import com.tugas.web.model.Lapang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LapangRepository extends JpaRepository<Lapang, Long> {
}
