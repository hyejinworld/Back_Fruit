package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Board;
import web.mvc.dto.BoardReq;
import web.mvc.dto.BoardRes;
import web.mvc.exception.BoardSearchNotException;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.BoardRepository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService{
	private final BoardRepository boardRepository;

	@Transactional
	public Board addBoard(BoardReq boardReq) {
		System.out.println("boardReq = " + boardReq);

		Board  board = boardReq.toBoard(boardReq); // dto --> Entity 변환
		System.out.println("board = " + board.toString());

		return boardRepository.save(board); //저장=insert
	}
	

	@Transactional(readOnly = true)
	public Board findBoard(Long id) throws BoardSearchNotException {
		return boardRepository.findById(id)
				.orElseThrow(()->new BoardSearchNotException(ErrorCode.NOTFOUND_NO));

	}
	
	@Transactional(readOnly = true)
	public Page<BoardRes> findAllBoard(int page, int size) throws BoardSearchNotException {
		Pageable pageable = PageRequest.of(page, size);
		// use repository's pageable join to avoid N+1
		Page<Board> boardPage = boardRepository.join(pageable);

		System.out.println("--------------------------------------------");
		if (boardPage == null || boardPage.isEmpty())
			return Page.empty();  // ← 예외 대신 빈 페이지 반환

		return boardPage.map(BoardRes::new);
	}
	
	@Transactional
	public Board updateBoard(Long id,BoardReq board)  throws DMLException {

		Board boardEntity = boardRepository.findById(id)
				      .orElseThrow(()->new DMLException(ErrorCode.UPDATE_FAILED));
		
		boardEntity.setTitle(board.getTitle());
		boardEntity.setContent(board.getContent());

		return boardEntity;
	}
	
	@Transactional
	public String deleteBoard(Long id) {
		System.out.println("id = " + id);
		boardRepository.findById(id).orElseThrow(()->
				new DMLException(ErrorCode.DELETE_FAILED));

		boardRepository.deleteById(id);

		return "ok";
	}
}
