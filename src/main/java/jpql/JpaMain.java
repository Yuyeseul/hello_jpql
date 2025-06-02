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
            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            String query = "select m from Member m where m.id = :memberId"; // 엔티티 직접 사용 - 기본키 값
            String query2 = "select m from Member m where m.team = :team"; // 엔티티 직접 사용 - 외래키 값

            Member findMember = em.createQuery(query, Member.class)
                    .setParameter("memberId", member1.getId())
                    .getSingleResult();

            System.out.println("findMember = " + findMember);

            List<Member> findMember2 = em.createQuery(query2, Member.class)
                    .setParameter("team", teamA)
                    .getResultList();

            for (Member member : findMember2) {
                System.out.println("member = " + member);
            }

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