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

            String query1 = "select distinct t from Team t join fetch t.members"; // 페이징 API 사용 X
            String query2 = "select t from Team t ";

            List<Team> resultList = em.createQuery(query2, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            System.out.println("resultList = " + resultList.size());

            for (Team team : resultList) {
                System.out.println("team : " + team.getName() + " | members :  " + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println(" -> member = " + member);
                }
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