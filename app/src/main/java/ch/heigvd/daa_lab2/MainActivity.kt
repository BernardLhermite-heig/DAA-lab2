package ch.heigvd.daa_lab2

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
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
    private lateinit var workerGroup: Group
    private lateinit var studentGroup: Group
    private lateinit var radGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        val nationalities = resources.getStringArray(R.array.nationalities).toList()
        val sectors = resources.getStringArray(R.array.sectors).toList()

        spnNationality.adapter =
            ArrayAdapterWithDefaultValue(
                this,
                android.R.layout.simple_list_item_1,
                nationalities,
                resources.getString(R.string.nationality_empty)
            )
        spnSector.adapter =
            ArrayAdapterWithDefaultValue(
                this,
                android.R.layout.simple_list_item_1,
                sectors,
                resources.getString(R.string.sectors_empty)
            )

        spnNationality.setSelection(-1)

        txtDate.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker()
            }
        }
        txtDate.keyListener = null

        btnDatePicker.setOnClickListener {
            showDatePicker()
        }

        btnSubmit.setOnClickListener {
            createPerson()
        }

        btnReset.setOnClickListener {
            resetFields()
        }

        radGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.main_occupation_student -> {
                    workerGroup.visibility = View.GONE
                    studentGroup.visibility = View.VISIBLE
                }
                R.id.main_occupation_worker -> {
                    workerGroup.visibility = View.VISIBLE
                    studentGroup.visibility = View.GONE
                }
            }
        }
    }

    private fun showDatePicker() {
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

        val selectedDate = Calendar.getInstance()
        if (txtDate.text.isNotEmpty()) {
            val date = LocalDate.parse(txtDate.text, formatter)
            selectedDate.set(date.year, date.monthValue - 1, date.dayOfMonth)
        }

        val calendarConstraints = CalendarConstraints.Builder()
            .setOpenAt(selectedDate.timeInMillis)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(calendarConstraints.build())
            .setSelection(selectedDate.timeInMillis)
            .build()

        datePicker.show(supportFragmentManager, null)

        datePicker.addOnPositiveButtonClickListener {
            val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
            txtDate.setText(date.format(formatter))
        }
    }

    private fun toCalendar(dateStr: String): Calendar {
        val date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd MMM yyyy"))
        return Calendar.getInstance().apply {
            set(date.year, date.monthValue - 1, date.dayOfMonth)
        }
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
        radGroup = findViewById(R.id.main_occupation_radioGroup)
        studentGroup = findViewById(R.id.group_student)
        workerGroup = findViewById(R.id.group_worker)
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
            return
        }

        val person: Person
        when (radGroup.checkedRadioButtonId) {
            R.id.main_occupation_student -> { // student
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
            }
            R.id.main_occupation_worker -> { // worker
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
            }
            else -> {
                Toast.makeText(this, "Please select a type", Toast.LENGTH_SHORT).show()
                return
            }
        }
        println(person)
    }

    private fun resetFields() {
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