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
            member.setUsername(null);
            member.setAge(20);
            member.setType(MemberType.ADMIN);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            // 기본 case 식
            String query = "select " +
                    "case when m.age <= 10 then '학생요금' " +
                    "     when m.age >= 60 then '경로요금' " +
                    "     else '일반요금' " +
                    "     end " +
                    "from Member m ";

            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();

            for (String result : resultList) {
                System.out.println("result : "+result);
            }

            // coalesce -> 값이 null 이 아니면 반환, null 이면 '이름 없는 회원'
            // nullif() -> 두 값이 같으면 null
            String query2 = "select coalesce(m.username, '이름 없는 회원') from Member m ";

            List<String> resultList2 = em.createQuery(query2, String.class)
                    .getResultList();

            for (String result2 : resultList2) {
                System.out.println("result2 : "+result2);
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