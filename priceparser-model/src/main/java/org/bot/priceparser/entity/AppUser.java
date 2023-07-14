package org.bot.priceparser.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bot.priceparser.entity.enums.TelegramUserState;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "app_user")
/*
называется AppUser, чтобы избежать путаницы,
т.к. есть объект TelegramUser, который тоже используется в этом приложении
 */
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long telegramUserId;
    @CreationTimestamp
    LocalDateTime firstLoginDate;
    String firstName;
    String lastName;
    String userName;
    String email;
    Boolean isActive;
    @Enumerated(EnumType.STRING)
    TelegramUserState state;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    List<Item> items = new ArrayList<>();
}
