package me.pixka.update.r;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import me.pixka.update.d.Updatedata;
@Repository
public interface UpdatedateRepo extends CrudRepository<Updatedata, Long> {

}
