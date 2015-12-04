package naree.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import naree.db.domain.Member;
import naree.db.mapper.HeightMapper;
import naree.db.mapper.MemberMapper;
import naree.util.factory.ConnectionFactory;

@Repository
public class MemberDaoImpl implements MemberDao {

	private static final Logger logger = LoggerFactory.getLogger(MemberDaoImpl.class);
	
	@Override
	public int selectMemberExistByFacebook_id(String facebook_id) {
		SqlSession sqlSession = ConnectionFactory.getInstance().getSqlSession();
		int result = 0;
		try{
			MemberMapper memberMapper = sqlSession.getMapper(MemberMapper.class);
			result = memberMapper.selectMemberExistByFacebook_id(facebook_id);
		}finally{
			sqlSession.close();
		}
		return result;
	}

	@Override
	public int insertMember(Member member) {
		SqlSession sqlSession = ConnectionFactory.getInstance().getSqlSession();
		int result = 0;
		try{
			MemberMapper memberMapper = sqlSession.getMapper(MemberMapper.class);
			result = memberMapper.insertMember(member);
		}finally{
			sqlSession.commit();
			sqlSession.close();
		}
		return result;
	}

	@Override
	public int selectMemberExistByKakao_id(String kakao_id) {
		SqlSession sqlSession = ConnectionFactory.getInstance().getSqlSession();
		int result = 0;
		try{
			MemberMapper memberMapper = sqlSession.getMapper(MemberMapper.class);
			result = memberMapper.selectMemberExistByKakao_id(kakao_id);
		}finally{
			sqlSession.close();
		}
		return result;
	}

	@Override
	public int selectMemberExistByEmail(String email) {
		SqlSession sqlSession = ConnectionFactory.getInstance().getSqlSession();
		int result = 0;
		try{
			MemberMapper memberMapper = sqlSession.getMapper(MemberMapper.class);
			result = memberMapper.selectMemberExistByEmail(email);
		}finally{
			sqlSession.close();
		}
		return result;
	}

	@Override
	public int insertJoinAgre(Member member) {
		SqlSession sqlSession = ConnectionFactory.getInstance().getSqlSession();
		int result = 0;
		try{
			MemberMapper memberMapper = sqlSession.getMapper(MemberMapper.class);
			result = memberMapper.insertJoinAgre(member);
		}finally{
			sqlSession.commit();
			sqlSession.close();
		}
		return result;
	}

	@Override
	public Member selectMemberByEmail(String email) {
		SqlSession sqlSession = ConnectionFactory.getInstance().getSqlSession();
		Member result;
		try{
			MemberMapper memberMapper = sqlSession.getMapper(MemberMapper.class);
			result = memberMapper.selectMemberByEmail(email);
		}finally{
			sqlSession.close();
		}
		return result;
	}

}