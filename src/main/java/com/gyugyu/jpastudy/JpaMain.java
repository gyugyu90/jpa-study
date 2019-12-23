package com.gyugyu.jpastudy;

import com.gyugyu.jpastudy.model.Board;
import com.gyugyu.jpastudy.model.Member;
import com.gyugyu.jpastudy.model.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * @author holyeye
 */
public class JpaMain {

    public static void main(String[] args) {

        //엔티티 매니저 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성

        EntityTransaction tx = em.getTransaction(); //트랜잭션 기능 획득

        try {


            tx.begin(); //트랜잭션 시작
//            logic(em);  //비즈니스 로직
            testSave(em);
//            queryLogicJoin(em);
//            updateRelation(em);
//            deleteRelation(em);
            biDirection(em);
            tx.commit();//트랜잭션 커밋


        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback(); //트랜잭션 롤백
        } finally {
            em.close(); //엔티티 매니저 종료
        }

        emf.close(); //엔티티 매니저 팩토리 종료
    }

    private static void logic(EntityManager em) {

        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("지한");
        member.setAge(2);

        //등록
        em.persist(member);

        //수정
        member.setAge(20);

        //한 건 조회
        Member findMember = em.find(Member.class, id);
        System.out.println("findMember=" + findMember.getUsername() + ", age=" + findMember.getAge());

        //목록 조회
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        System.out.println("members.size=" + members.size());

        //삭제
        em.remove(member);

    }

    private static void logicBoard(EntityManager em) {
        Board board = new Board();
        em.persist(board);
        System.out.println("board.id = " + board.getId());
    }

    private static void testSave(EntityManager em) {
        // 팀 1 저장
        Team team1 = new Team();
        team1.setId("team1");
        team1.setName("팀1");
        em.persist(team1);

        // 회원 1 저장
        Member member1 = new Member();
        member1.setId("member1");
        member1.setUsername("회원1");
        member1.setAge(20);
        member1.setTeam(team1);
        em.persist(member1);

        Member member2 = new Member();
        member2.setId("member2");
        member2.setUsername("회원2");
        member2.setAge(20);
        member2.setTeam(team1);
        em.persist(member2);
    }

    private static void queryLogicJoin(EntityManager em) {
        String jpql = "select m from Member m join m.team t where t.name=:teamName";

        List<Member> resultList = em.createQuery(jpql, Member.class)
                .setParameter("teamName", "팀1")
                .getResultList();

        for (Member member: resultList) {
            System.out.println("[query] member.username = " + member.getUsername());
        }
    }

    private static void updateRelation(EntityManager em) {
        // 새로운 팀
        Team team2 = new Team();
        team2.setId("team2");
        team2.setName("팀2");
        em.persist(team2);

        // 회원1에 새로운 팀2 설정
        Member member = em.find(Member.class, "member1");
        member.setTeam(team2);
    }

    private static void deleteRelation(EntityManager em) {
        Member member1 = em.find(Member.class, "member1");
        member1.setTeam(null); // 연관관계 제거
    }

    private static void biDirection(EntityManager em) {
        Team team = em.find(Team.class, "team1");
        List<Member> members = team.getMembers(); // (팀 -> 회원) 객체 그래프 탐색

        for(Member member: members) {
            System.out.println("member.username = " + member.getUsername());
        }
    }

}
