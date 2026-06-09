package web.mvc.service;



import web.mvc.domain.Board;
import web.mvc.dto.BoardReq;
import web.mvc.dto.BoardRes;
import web.mvc.exception.BoardSearchNotException;
import web.mvc.exception.DMLException;

import org.springframework.data.domain.Page;

public interface BoardService {
    Board findBoard(Long id) throws BoardSearchNotException;

    Page<BoardRes> findAllBoard(int page, int size) throws BoardSearchNotException ;
     Board addBoard(BoardReq board);

    Board updateBoard(Long id,BoardReq board)throws DMLException; ;

     String deleteBoard(Long id);
}
