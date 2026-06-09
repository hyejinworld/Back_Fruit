package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import web.mvc.domain.Board;
import java.util.List;



public interface BoardRepository extends JpaRepository<Board, Long>{
//   @Query(value = "select b from Board b join fetch b.member", countQuery = "select count(b) from Board b")
//    Page<Board> join(Pageable pageable);

    @Query(value = "select b from Board b join fetch b.member order by b.id desc",
            countQuery = "select count(b) from Board b")
    Page<Board> join(Pageable pageable);

    @Query("select b from Board b join fetch b.member")
    List<Board> join();
}
