package ru.otus.java_2018_08.student.hw11.morm;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "user")
public class UserDataSet extends DataSet {
    private String name;
    private int age;
    @OneToOne(cascade = CascadeType.ALL)
    private AddressDataSet address;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private List<PhoneDataSet> phoneList;
}
