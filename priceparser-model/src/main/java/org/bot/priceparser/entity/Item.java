package org.bot.priceparser.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String link;
    Long startPrice;
    Long currentPrice;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appUser_id")
    private AppUser appUser;
}
