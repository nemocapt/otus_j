package ru.otus.java_2018_08.student.hw14.orm;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "user")
public class UserDataSet extends DataSet{
    private String name;
}
