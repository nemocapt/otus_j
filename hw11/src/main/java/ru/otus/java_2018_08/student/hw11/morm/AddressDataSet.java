package ru.otus.java_2018_08.student.hw11.morm;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "address")
public class AddressDataSet extends DataSet {
    private String street;
}
