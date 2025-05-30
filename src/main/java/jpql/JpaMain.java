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
            member.setUsername("yu");
            member.setAge(20);
            member.setType(MemberType.ADMIN);

            Member member2 = new Member();
            member2.setUsername("ys");

            member.setTeam(team);

            em.persist(member);
            em.persist(member2);

            em.flush();
            em.clear();


            String query = "select group_concat(m.username) from Member m "; // == function('group_concat', m.username)

            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();


            for (String result : resultList) {
                System.out.println("result : " + result);
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