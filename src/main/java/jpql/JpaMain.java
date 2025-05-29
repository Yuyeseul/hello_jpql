package jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("memberA");
            member.setAge(20);
            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

//            "select m from Member m inner join m.team t"; // 내부 조인
//            "select m from Member m left join m.team "; // 외부 조인
//            "select m from Member m, Team t where m.username = t.name"; // 세타 조인
//            "select m from Member m left join m.team t on t.name = 'teamA'"; // on절 - 조인 대상 필터링
            String query = "select m from Member m join Team t on m.username = t.name"; // on절 - 연관관계 없는 엔티티 외부 조인

            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            System.out.println("result = " + result.size());

            tx.commit();
        } catch (
                Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}