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

            // 상태 필드
            String query = "select m from Member m join fetch m.team"; // team 프록시 X
            String query2 = "select distinct t from Team t join fetch t.members";
            List<Member> resultList = em.createQuery(query, Member.class)
                    .getResultList();
            for (Member member : resultList) {
                System.out.println("result : " + member.getUsername() + ", " + member.getTeam().getName());
            }

            List<Team> resultList2 = em.createQuery(query2, Team.class)
                    .getResultList();
            for (Team team : resultList2) {
                System.out.println("result : " + team.getName() + ", " + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println(" -> member = " + member);
                }
            }

            // "select m from Member m"
            // member1 , teamA (SQL)
            // member2 , teamA (1차 캐시)
            // member3 , teamB (SQL)

            // 회원 100명 -> 쿼리 100번 N(100번)+1(첫쿼리)

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