package com.example.akela.swim.crm.graphs;

import com.example.akela.swim.crm.repository.SubscriptionPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentsGraphsService {

    private final SubscriptionPaymentRepository repo;

    public PaymentsStatsDTO getStats(String q, LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("from și to sunt obligatorii pentru grafice");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Interval invalid");
        }

        String qq = normalize(q);

        PaymentsTotalProjection totalsP = repo.getTotalsStrict(qq, from, to);

        double totalAmount = (totalsP != null && totalsP.getTotalAmount() != null)
                ? totalsP.getTotalAmount()
                : 0d;

        long paymentsCount = (totalsP != null && totalsP.getPaymentsCount() != null)
                ? totalsP.getPaymentsCount()
                : 0L;

        long daysInRange = ChronoUnit.DAYS.between(from, to) + 1; // inclusiv
        double avgPerDay = daysInRange > 0 ? (totalAmount / daysInRange) : 0d;

        PaymentsBucket bucket = pickBucket(daysInRange);

        List<BucketTotalProjection> seriesP = switch (bucket) {
            case DAY -> repo.getSeriesByDayStrict(qq, from, to);
            case WEEK -> repo.getSeriesByWeekStrict(qq, from, to);
            case MONTH -> repo.getSeriesByMonthStrict(qq, from, to);
        };

        // top tipuri (recomand top=6)
        int top = 6;
        List<TypeTotalProjection> typeP = repo.getTotalsByTypeTopStrict(qq, from, to, top);

        // best day: cel mai bun din daily real (nu neapărat din series weekly/monthly)
        // dacă bucket != DAY, tot vrei bestDay real pe zi:
        List<DailyTotalProjection> dailyP = repo.getDailyTotalsStrict(qq, from, to);
        LocalDate bestDay = null;
        double bestDayAmount = 0d;
        for (DailyTotalProjection p : dailyP) {
            double v = p.getTotal() != null ? p.getTotal() : 0d;
            if (bestDay == null || v > bestDayAmount) {
                bestDay = p.getDay();
                bestDayAmount = v;
            }
        }

        PaymentsStatsDTO dto = new PaymentsStatsDTO();
        dto.setFrom(from);
        dto.setTo(to);
        dto.setQ(qq);

        dto.setTotalAmount(totalAmount);
        dto.setPaymentsCount(paymentsCount);

        dto.setAvgPerDay(avgPerDay);
        dto.setBestDay(bestDay);
        dto.setBestDayAmount(bestDayAmount);

        dto.setBucket(bucket);

        dto.setSeries(
                seriesP.stream()
                        .map(p -> new BucketPointDTO(
                                p.getBucket(),
                                p.getTotal() != null ? p.getTotal() : 0d
                        ))
                        .toList()
        );

        dto.setByTypeTop(
                typeP.stream()
                        .map(p -> new TypePointDTO(
                                p.getTypeName(),
                                p.getTotal() != null ? p.getTotal() : 0d,
                                p.getCount() != null ? p.getCount() : 0L
                        ))
                        .toList()
        );

        return dto;
    }

    private PaymentsBucket pickBucket(long daysInRange) {
        if (daysInRange <= 60) return PaymentsBucket.DAY;
        if (daysInRange <= 365) return PaymentsBucket.WEEK;
        return PaymentsBucket.MONTH;
    }

    private String normalize(String q) {
        if (q == null) return null;
        String s = q.trim();
        return s.isEmpty() ? null : s;
    }
}
