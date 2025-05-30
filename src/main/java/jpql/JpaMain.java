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
            member.setTeam(team);

            Member member2 = new Member();
            member2.setUsername("ys");
            member2.setTeam(team);

            em.persist(member);
            em.persist(member2);

            em.flush();
            em.clear();

            // 상태 필드
            String query = "select m.username from Member m";
            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();
            for (String result : resultList) {
                System.out.println("result : " + result);
            }

            // 단일 값 연관 필드
            String query2 = "select m.team from Member m";
            List<Team> resultList2 = em.createQuery(query2, Team.class).getResultList();
            for (Team result2 : resultList2) {
                System.out.println("result2 : " + result2);
            }

            // 컬렉션 값 연관 필드
            String query3 = "select t.members from Team t";
            List<Member> resultList3 = em.createQuery(query3, Member.class).getResultList();
            for (Member result3 : resultList3) {
                System.out.println("result3 : " + result3);
            }

            String query4 = "select size(t.members) from Team t";
            Integer result4 = em.createQuery(query4, Integer.class).getSingleResult();
            System.out.println("result4 : " + result4);

            // 컬렉션 값 연관 필드 - 명시적 조인을 통해 탐색 가능
            String query5 = "select m.username from Team t join t.members m";

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