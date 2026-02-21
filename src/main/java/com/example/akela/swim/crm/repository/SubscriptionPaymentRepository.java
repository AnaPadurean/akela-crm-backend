package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.SubscriptionPaymentEntity;
import com.example.akela.swim.crm.graphs.BucketTotalProjection;
import com.example.akela.swim.crm.graphs.DailyTotalProjection;
import com.example.akela.swim.crm.graphs.PaymentsTotalProjection;
import com.example.akela.swim.crm.graphs.TypeTotalProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionPaymentRepository extends JpaRepository<SubscriptionPaymentEntity, Long> {

    boolean existsBySubscription_SubscriptionId(Long subscriptionId);

    List<SubscriptionPaymentEntity> findAllByPaidDateBetweenOrderByPaidDateDesc(LocalDate from, LocalDate to);

    List<SubscriptionPaymentEntity> findAllByOrderByPaidDateDesc();
    Optional<SubscriptionPaymentEntity> findBySubscription_SubscriptionId(Long subscriptionId);
    @Query(
            value = """
        SELECT p.*
        FROM akl_subscription_payments p
        JOIN akl_subscriptions s ON s.subscription_id = p.subscription_id
        JOIN akl_children c ON c.child_id = s.child_id
        JOIN akl_subscription_plans pl ON pl.subscription_plan_id = s.subscription_plan_id
        JOIN akl_dd_subscription_type st ON st.subscription_type_id = pl.subscription_type_id
        WHERE
          (CAST(:from AS date) IS NULL OR p.paid_date >= CAST(:from AS date))
          AND (CAST(:to AS date) IS NULL OR p.paid_date <= CAST(:to AS date))
          AND (
            :q IS NULL OR

            -- copil: "Popescu Ana"
            (c.child_last_name || ' ' || c.child_first_name) ILIKE ('%' || :q || '%')

            -- tip abonament: "Sărituri în apă"
            OR st.name ILIKE ('%' || :q || '%')

            -- plan exact cum îl afișezi în UI: "Sărituri în apă (4 ședințe)"
            OR (st.name || ' (' || pl.sessions::text || ' ședințe)') ILIKE ('%' || :q || '%')

            -- doar numărul de ședințe: "4"
            OR pl.sessions::text ILIKE ('%' || :q || '%')

            -- preț plan (dacă vrei să poți căuta "900")
            OR COALESCE(pl.price, 0)::text ILIKE ('%' || :q || '%')

            -- sumă încasată (dacă vrei să poți căuta "900" și la payment amount)
            OR COALESCE(p.amount, 0)::text ILIKE ('%' || :q || '%')

            -- observații
            OR COALESCE(p.observations, '') ILIKE ('%' || :q || '%')
          )
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM akl_subscription_payments p
        JOIN akl_subscriptions s ON s.subscription_id = p.subscription_id
        JOIN akl_children c ON c.child_id = s.child_id
        JOIN akl_subscription_plans pl ON pl.subscription_plan_id = s.subscription_plan_id
        JOIN akl_dd_subscription_type st ON st.subscription_type_id = pl.subscription_type_id
        WHERE
          (CAST(:from AS date) IS NULL OR p.paid_date >= CAST(:from AS date))
          AND (CAST(:to AS date) IS NULL OR p.paid_date <= CAST(:to AS date))
          AND (
            :q IS NULL OR

            (c.child_last_name || ' ' || c.child_first_name) ILIKE ('%' || :q || '%')
            OR st.name ILIKE ('%' || :q || '%')
            OR (st.name || ' (' || pl.sessions::text || ' ședințe)') ILIKE ('%' || :q || '%')
            OR pl.sessions::text ILIKE ('%' || :q || '%')
            OR COALESCE(pl.price, 0)::text ILIKE ('%' || :q || '%')
            OR COALESCE(p.amount, 0)::text ILIKE ('%' || :q || '%')
            OR COALESCE(p.observations, '') ILIKE ('%' || :q || '%')
          )
        """,
            nativeQuery = true
    )
    Page<SubscriptionPaymentEntity> search(
            @Param("q") String q,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            Pageable pageable
    );

    static final String FILTERS_STRICT = """
  WHERE
    p.paid_date >= :from
    AND p.paid_date <= :to
    AND (
      :q IS NULL OR :q = '' OR
      (c.child_last_name || ' ' || c.child_first_name) ILIKE ('%' || :q || '%')
      OR st.name ILIKE ('%' || :q || '%')
      OR st.code ILIKE ('%' || :q || '%')
      OR (st.name || ' (' || pl.sessions::text || ' ședințe)') ILIKE ('%' || :q || '%')
      OR pl.sessions::text ILIKE ('%' || :q || '%')
      OR COALESCE(pl.price, 0)::text ILIKE ('%' || :q || '%')
      OR COALESCE(p.amount, 0)::text ILIKE ('%' || :q || '%')
      OR COALESCE(p.observations, '') ILIKE ('%' || :q || '%')
    )
""";


    @Query(value = """
  SELECT
    COALESCE(SUM(p.amount), 0) AS totalAmount,
    COUNT(*) AS paymentsCount
  FROM akl_subscription_payments p
  JOIN akl_subscriptions s ON s.subscription_id = p.subscription_id
  JOIN akl_children c ON c.child_id = s.child_id
  JOIN akl_subscription_plans pl ON pl.subscription_plan_id = s.subscription_plan_id
  JOIN akl_dd_subscription_type st ON st.subscription_type_id = pl.subscription_type_id
  """ + FILTERS_STRICT, nativeQuery = true)
    PaymentsTotalProjection getTotalsStrict(
            @Param("q") String q,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );


    @Query(value = """
  SELECT
    p.paid_date AS day,
    COALESCE(SUM(p.amount), 0) AS total
  FROM akl_subscription_payments p
  JOIN akl_subscriptions s ON s.subscription_id = p.subscription_id
  JOIN akl_children c ON c.child_id = s.child_id
  JOIN akl_subscription_plans pl ON pl.subscription_plan_id = s.subscription_plan_id
  JOIN akl_dd_subscription_type st ON st.subscription_type_id = pl.subscription_type_id
  """ + FILTERS_STRICT + """
  GROUP BY p.paid_date
  ORDER BY p.paid_date ASC
""", nativeQuery = true)
    List<DailyTotalProjection> getDailyTotalsStrict(
            @Param("q") String q,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );


    @Query(value = """
  SELECT
    st.name AS typeName,
    COALESCE(SUM(p.amount), 0) AS total,
    COUNT(*) AS count
  FROM akl_subscription_payments p
  JOIN akl_subscriptions s ON s.subscription_id = p.subscription_id
  JOIN akl_children c ON c.child_id = s.child_id
  JOIN akl_subscription_plans pl ON pl.subscription_plan_id = s.subscription_plan_id
  JOIN akl_dd_subscription_type st ON st.subscription_type_id = pl.subscription_type_id
  """ + FILTERS_STRICT + """
  GROUP BY st.name
  ORDER BY total DESC
""", nativeQuery = true)
    List<TypeTotalProjection> getTotalsByTypeStrict(
            @Param("q") String q,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );


    @Query(value = """
WITH days AS (
  SELECT generate_series(CAST(:from AS date), CAST(:to AS date), interval '1 day')::date AS day
),
base AS (
  SELECT p.paid_date::date AS day, SUM(p.amount) AS total
  FROM akl_subscription_payments p
  JOIN akl_subscriptions s ON s.subscription_id = p.subscription_id
  JOIN akl_children c ON c.child_id = s.child_id
  JOIN akl_subscription_plans pl ON pl.subscription_plan_id = s.subscription_plan_id
  JOIN akl_dd_subscription_type st ON st.subscription_type_id = pl.subscription_type_id
  """ + FILTERS_STRICT + """
  GROUP BY p.paid_date::date
)
SELECT
  to_char(d.day, 'YYYY-MM-DD') AS bucket,
  COALESCE(b.total, 0) AS total
FROM days d
LEFT JOIN base b ON b.day = d.day
ORDER BY d.day ASC
""", nativeQuery = true)
    List<BucketTotalProjection> getSeriesByDayStrict(
            @Param("q") String q,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );


    @Query(value = """
SELECT
  to_char(date_trunc('week', p.paid_date), 'IYYY-"W"IW') AS bucket,
  COALESCE(SUM(p.amount), 0) AS total
FROM akl_subscription_payments p
JOIN akl_subscriptions s ON s.subscription_id = p.subscription_id
JOIN akl_children c ON c.child_id = s.child_id
JOIN akl_subscription_plans pl ON pl.subscription_plan_id = s.subscription_plan_id
JOIN akl_dd_subscription_type st ON st.subscription_type_id = pl.subscription_type_id
""" + FILTERS_STRICT + """
GROUP BY date_trunc('week', p.paid_date)
ORDER BY date_trunc('week', p.paid_date) ASC
""", nativeQuery = true)
    List<BucketTotalProjection> getSeriesByWeekStrict(
            @Param("q") String q,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );


    @Query(value = """
SELECT
  to_char(date_trunc('month', p.paid_date), 'YYYY-MM') AS bucket,
  COALESCE(SUM(p.amount), 0) AS total
FROM akl_subscription_payments p
JOIN akl_subscriptions s ON s.subscription_id = p.subscription_id
JOIN akl_children c ON c.child_id = s.child_id
JOIN akl_subscription_plans pl ON pl.subscription_plan_id = s.subscription_plan_id
JOIN akl_dd_subscription_type st ON st.subscription_type_id = pl.subscription_type_id
""" + FILTERS_STRICT + """
GROUP BY date_trunc('month', p.paid_date)
ORDER BY date_trunc('month', p.paid_date) ASC
""", nativeQuery = true)
    List<BucketTotalProjection> getSeriesByMonthStrict(
            @Param("q") String q,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query(value = """
WITH t AS (
  SELECT
    st.name AS typeName,
    COALESCE(SUM(p.amount), 0) AS total,
    COUNT(*) AS count
  FROM akl_subscription_payments p
  JOIN akl_subscriptions s ON s.subscription_id = p.subscription_id
  JOIN akl_children c ON c.child_id = s.child_id
  JOIN akl_subscription_plans pl ON pl.subscription_plan_id = s.subscription_plan_id
  JOIN akl_dd_subscription_type st ON st.subscription_type_id = pl.subscription_type_id
  """ + FILTERS_STRICT + """
  GROUP BY st.name
),
ranked AS (
  SELECT *, ROW_NUMBER() OVER (ORDER BY total DESC) AS rn
  FROM t
),
topn AS (
  SELECT typeName, total, count
  FROM ranked
  WHERE rn <= :top
),
others AS (
  SELECT
    'Altele' AS typeName,
    COALESCE(SUM(total), 0) AS total,
    COALESCE(SUM(count), 0) AS count
  FROM ranked
  WHERE rn > :top
)
SELECT * FROM topn
UNION ALL
SELECT * FROM others
WHERE (SELECT COUNT(*) FROM ranked WHERE rn > :top) > 0
ORDER BY total DESC
""", nativeQuery = true)
    List<TypeTotalProjection> getTotalsByTypeTopStrict(
            @Param("q") String q,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("top") int top
    );

}

