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

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(20);
            em.persist(member);

            em.flush();
            em.clear();

            // 영속성 컨텍스트 관리
//            List<Member> result = em.createQuery("select m from Member m", Member.class)
//                    .getResultList();
//            Member findMember = result.get(0);
//            findMember.setAge(25);

            // 프로젝션 여러 값 조회 1. Query 타입으로 조회
//            List resultList = em.createQuery("select m.username, m.age from Member m").getResultList();
//            Object o = resultList.get(0);
//            Object[] result2 = (Object[]) o;
//            System.out.println("username = " + result2[0]);
//            System.out.println("age = " + result2[1]);

            // 프로젝션 여러 값 조회 2. Object[] 타입으로 조회
//            List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m").getResultList();
//            Object[] result2 = resultList.get(0);
//            System.out.println("username = " + result2[0]);
//            System.out.println("age = " + result2[1]);

            // 프로젝션 여러 값 조회 3. new 명령어로 조회
            List<MemberDTO> resultList = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class).getResultList();
            MemberDTO memberDTO = resultList.get(0);
            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());

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