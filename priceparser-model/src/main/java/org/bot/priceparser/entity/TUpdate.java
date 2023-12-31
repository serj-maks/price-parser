package org.bot.priceparser.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import lombok.experimental.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "telegram_update")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
/*
данная сущность описывает класс Update библиотеки телеграма, но т.к. данный класс
уже существует, название класса: TUpdate
*/
public class TUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    Update update;
}
