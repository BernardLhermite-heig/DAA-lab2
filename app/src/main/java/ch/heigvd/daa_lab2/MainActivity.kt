package ch.heigvd.daa_lab2

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    private var type = -1
    private lateinit var btnSubmit: Button
    private lateinit var btnDatePicker: ImageButton
    private lateinit var btnReset: Button
    private lateinit var txtDate: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtRemark: EditText
    private lateinit var txtSchool: EditText
    private lateinit var txtGraduationYear: EditText
    private lateinit var txtCompany: EditText
    private lateinit var txtExperience: EditText
    private lateinit var spnNationality: Spinner
    private lateinit var spnSector: Spinner
    private lateinit var radStudent: RadioButton
    private lateinit var radWorker: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        txtDate.setOnClickListener {
            showDatePicker()
        }
        txtDate.keyListener = null

        btnDatePicker.setOnClickListener {
            showDatePicker()
        }

        btnSubmit.setOnClickListener {
            createPerson()
        }

        btnReset.setOnClickListener {
            reset()
        }
    }

    private fun showDatePicker() {
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

        val selectedDate = if (txtDate.text.isNotEmpty()) {
            LocalDate.parse(txtDate.text.toString(), formatter)
        } else {
            LocalDate.now()
        }

        val selectedDateInMilli =
            selectedDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

        val calendarConstraints = CalendarConstraints.Builder()
            .setOpenAt(selectedDateInMilli)

        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
        datePickerBuilder.setCalendarConstraints(calendarConstraints.build())
        datePickerBuilder.setSelection(selectedDateInMilli)

        val datePicker = datePickerBuilder.build()
        datePicker.show(supportFragmentManager, null)

        datePicker.addOnPositiveButtonClickListener {
            val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
            txtDate.setText(date.format(formatter))
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

    private fun init() {
        btnSubmit = findViewById(R.id.ok_button)
        btnDatePicker = findViewById(R.id.main_birthDate_button)
        btnReset = findViewById(R.id.cancel_button)
        txtDate = findViewById(R.id.main_birthDate_editText)
        txtLastName = findViewById(R.id.main_lastName_editText)
        txtFirstName = findViewById(R.id.main_firstName_editText)
        txtEmail = findViewById(R.id.additional_email_editText)
        txtRemark = findViewById(R.id.additional_remarks_editText)
        txtSchool = findViewById(R.id.student_school_name_editText)
        txtGraduationYear = findViewById(R.id.student_diploma_year_editText)
        txtCompany = findViewById(R.id.worker_company_editText)
        txtExperience = findViewById(R.id.worker_experience_editText)
        spnNationality = findViewById(R.id.main_nationality_spinner)
        spnSector = findViewById(R.id.worker_sector_spinner)
        radStudent = findViewById(R.id.main_occupation_student)
        radWorker = findViewById(R.id.main_occupation_worker)
    }

    private fun createPerson() {
        val name = txtLastName.text.toString()
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
                    val department = spnSector.selectedItem.toString()
                    val seniority = txtExperience.text.toString().toInt()
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

    private fun reset() {
        txtLastName.text.clear()
        txtFirstName.text.clear()
        txtEmail.text.clear()
        txtRemark.text.clear()
        txtSchool.text.clear()
        txtGraduationYear.text.clear()
        txtCompany.text.clear()
        txtExperience.text.clear()
        spnNationality.setSelection(0)
        spnSector.setSelection(0)
        radStudent.isChecked = false
        radWorker.isChecked = false
    }
}