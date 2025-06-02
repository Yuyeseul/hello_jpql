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

//            em.flush();
//            em.clear();

            // query 날리면 flush 자동 호출 -> db 에 나이 0 으로 insert
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate(); // db 에 나이 20 으로 update (db에만 반영된거임)

            System.out.println("member1.getAge() = " + member1.getAge()); // 0살

            em.clear(); // em.clear() 안하면 영속성 컨텍스트에 남아있음 -> 나이 0으로 나옴

            Member findMember = em.find(Member.class, member1.getId()); // em.clear 후 db에서 가져옴 -> 나이 20
            System.out.println("member1.getAge() = " + findMember.getAge()); // 20살

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