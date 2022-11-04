package ch.heigvd.daa_lab2

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class MainActivity : AppCompatActivity() {
    private var type = -1
    private lateinit var btnSubmit : Button
    private lateinit var btnDatePicker : Button
    private lateinit var btnReset : Button
    private lateinit var txtDate : EditText
    private lateinit var txtName : EditText
    private lateinit var txtFirstName : EditText
    private lateinit var txtEmail : EditText
    private lateinit var txtRemark : EditText
    private lateinit var txtSchool : EditText
    private lateinit var txtGraduationYear : EditText
    private lateinit var txtCompany : EditText
    private lateinit var txtSeniority : EditText
    private lateinit var spnNationality : Spinner
    private lateinit var spnDepartment : Spinner
    private lateinit var radStudent : RadioButton
    private lateinit var radWorker : RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        btnDatePicker.setOnClickListener {
            var selectDate = if(txtDate.text.toString().isNotEmpty()) {
                Date(txtDate.text.toString())
            } else {
                Calendar.getInstance().time
            }

            val calendarConstraints = CalendarConstraints.Builder()
            calendarConstraints.setOpenAt(selectDate.month.toLong())

            val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
            datePickerBuilder.setCalendarConstraints(calendarConstraints.build())
            datePickerBuilder.setSelection(selectDate.time)

            val datePicker = datePickerBuilder.build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                txtDate.setText(Date(it).toString())
            }
        }

        btnSubmit.setOnClickListener {
            createPerson()
        }

        btnReset.setOnClickListener {
            reset()
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view) {
                radStudent ->
                    if (checked) {
                        type = 0
                    }
                radWorker ->
                    if (checked) {
                        type = 1
                    }
            }
        }
    }

    private fun toCalendar(dateStr: String): Calendar {
        val date = Date(dateStr)
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }

    private fun init(){
        btnSubmit = findViewById(R.id.submit)
        btnDatePicker = findViewById(R.id.datePicker)
        btnReset = findViewById(R.id.reset)
        txtDate = findViewById(R.id.dateLabel)
        txtName = findViewById(R.id.name)
        txtFirstName = findViewById(R.id.firstName)
        txtEmail = findViewById(R.id.email)
        txtRemark = findViewById(R.id.remark)
        txtSchool = findViewById(R.id.school)
        txtGraduationYear = findViewById(R.id.graduateYear)
        txtCompany = findViewById(R.id.company)
        txtSeniority = findViewById(R.id.seniority)
        spnNationality = findViewById(R.id.nationality)
        spnDepartment = findViewById(R.id.department)
        radStudent = findViewById(R.id.radio_student)
        radWorker = findViewById(R.id.radio_worker)
    }

    private fun createPerson(){
        val name = txtName.text.toString()
        val firstName = txtFirstName.text.toString()
        val nationality = spnNationality.selectedItem.toString()
        val email = txtEmail.text.toString()
        val remark = txtRemark.text.toString()
        val birthDay = txtDate.text.toString()

        if (name.isEmpty() || firstName.isEmpty() || birthDay.isEmpty() || nationality == "Sélectionner") {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        } else {
            val person: Person
            when (type) {
                0 -> { // student
                    val school = txtSchool.text.toString()
                    val graduateYear = txtGraduationYear.text.toString().toInt()
                    person = Student(
                        name,
                        firstName,
                        toCalendar(birthDay),
                        nationality,
                        school,
                        graduateYear,
                        email,
                        remark
                    )
                    println(person)
                }
                1 -> { // worker
                    val company = txtCompany.text.toString()
                    val department = spnDepartment.selectedItem.toString()
                    val seniority = txtSeniority.text.toString().toInt()
                    person = Worker(
                        name,
                        firstName,
                        toCalendar(birthDay),
                        nationality,
                        company,
                        department,
                        seniority,
                        email,
                        remark
                    )
                    println(person)
                }
                else -> Toast.makeText(this, "Please select a type", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun reset(){
        txtName.text.clear()
        txtFirstName.text.clear()
        txtEmail.text.clear()
        txtRemark.text.clear()
        txtSchool.text.clear()
        txtGraduationYear.text.clear()
        txtCompany.text.clear()
        txtSeniority.text.clear()
        spnNationality.setSelection(0)
        spnDepartment.setSelection(0)
        radStudent.isChecked = false
        radWorker.isChecked = false
    }
}