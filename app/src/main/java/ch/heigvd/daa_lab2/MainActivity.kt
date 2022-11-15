package ch.heigvd.daa_lab2

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.google.android.material.datepicker.*
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * Activité principale de l'application permettant de créer des personnes (étudiants ou employés).
 * Une personne existente peut être chargée à l'aide de la méthode [loadPerson] après la création de l'activité.
 * @author Marengo Stéphane, Friedli Jonathan, Silvestri Géraud
 */
class MainActivity : AppCompatActivity() {
    private lateinit var btnSubmit: Button
    private lateinit var btnDatePicker: ImageButton
    private lateinit var btnReset: Button
    private lateinit var txtBirthday: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtRemark: EditText
    private lateinit var txtUniversity: EditText
    private lateinit var txtGraduationYear: EditText
    private lateinit var txtCompany: EditText
    private lateinit var txtExperienceYear: EditText
    private lateinit var spnNationality: Spinner
    private lateinit var spnSector: Spinner
    private lateinit var radStudent: RadioButton
    private lateinit var radWorker: RadioButton
    private lateinit var workerGroup: Group
    private lateinit var studentGroup: Group
    private lateinit var radGroup: RadioGroup

    private lateinit var nationalityAdapter: ArrayAdapterWithDefaultValue<String?>
    private lateinit var sectorAdapter: ArrayAdapterWithDefaultValue<String?>
    private var selectedSector: String? = null
    private var selectedNationality: String? = null

    companion object {
        private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        nationalityAdapter = ArrayAdapterWithDefaultValue(
            this,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.nationalities).toList(),
            resources.getString(R.string.nationality_empty)
        )
        spnNationality.adapter = nationalityAdapter
        spnNationality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedNationality = nationalityAdapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedNationality = null
            }
        }

        sectorAdapter = ArrayAdapterWithDefaultValue(
            this,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.sectors).toList(),
            resources.getString(R.string.sectors_empty)
        )
        spnSector.adapter = sectorAdapter
        spnSector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedSector = sectorAdapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedSector = null
            }
        }

        txtBirthday.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker()
            }
        }
        txtBirthday.keyListener = null

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
                else -> {
                    workerGroup.visibility = View.VISIBLE
                    studentGroup.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showDatePicker() {
        val selectedDate = Calendar.getInstance()
        if (txtBirthday.text.isNotEmpty()) {
            val date = LocalDate.parse(txtBirthday.text, dateFormatter)
            selectedDate.set(date.year, date.monthValue - 1, date.dayOfMonth)
        }

        val minDateInMs =
            OffsetDateTime.now(ZoneOffset.UTC).minusYears(110).toInstant().toEpochMilli()

        val calendarValidators = CompositeDateValidator.allOf(
            listOf(
                DateValidatorPointBackward.now(),
                DateValidatorPointForward.from(minDateInMs)
            )
        )

        val calendarConstraints = CalendarConstraints.Builder()
            .setOpenAt(selectedDate.timeInMillis)
            .setEnd(MaterialDatePicker.thisMonthInUtcMilliseconds())
            .setValidator(calendarValidators)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(calendarConstraints.build())
            .setSelection(selectedDate.timeInMillis)
            .build()

        datePicker.show(supportFragmentManager, null)

        datePicker.addOnPositiveButtonClickListener {
            val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
            txtBirthday.setText(date.format(dateFormatter))
        }
    }

    private fun toCalendar(dateStr: String): Calendar {
        val date = LocalDate.parse(dateStr, dateFormatter)
        return Calendar.getInstance().apply {
            set(date.year, date.monthValue - 1, date.dayOfMonth)
        }
    }

    private fun init() {
        btnSubmit = findViewById(R.id.ok_button)
        btnDatePicker = findViewById(R.id.main_birthDate_button)
        btnReset = findViewById(R.id.cancel_button)
        txtBirthday = findViewById(R.id.main_birthDate_editText)
        txtLastName = findViewById(R.id.main_lastName_editText)
        txtFirstName = findViewById(R.id.main_firstName_editText)
        txtEmail = findViewById(R.id.additional_email_editText)
        txtRemark = findViewById(R.id.additional_remarks_editText)
        txtUniversity = findViewById(R.id.student_school_name_editText)
        txtGraduationYear = findViewById(R.id.student_diploma_year_editText)
        txtCompany = findViewById(R.id.worker_company_editText)
        txtExperienceYear = findViewById(R.id.worker_experience_editText)
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
        val birthday = txtBirthday.text.toString()
        val email = txtEmail.text.toString()
        val remarks = txtRemark.text.toString()

        if (name.isEmpty() || firstName.isEmpty() || birthday.isEmpty() || selectedNationality == null || email.isEmpty()) {
            showError(resources.getString(R.string.error_missing_fields))
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(resources.getString(R.string.error_invalid_email))
            return
        }

        val person: Person
        when (radGroup.checkedRadioButtonId) {
            R.id.main_occupation_student -> {
                val school = txtUniversity.text.toString()
                val graduateYear = txtGraduationYear.text.toString().toIntOrNull()

                if (school.isEmpty() || graduateYear == null) {
                    val errorMessage = resources.getString(R.string.error_missing_occupation_fields)
                    val sectionName = resources.getString(R.string.main_specific_students_title)
                    showError(errorMessage.format(sectionName))
                    return
                }

                if (graduateYear <= 0) {
                    val errorMessage = resources.getString(R.string.error_negative_or_zero_field)
                    val fieldName = resources.getString(R.string.main_specific_graduationyear_title)
                    showError(errorMessage.format(fieldName))
                    return
                }

                person = Student(
                    name,
                    firstName,
                    toCalendar(birthday),
                    selectedNationality!!,
                    school,
                    graduateYear,
                    email,
                    remarks
                )
            }
            R.id.main_occupation_worker -> {
                val company = txtCompany.text.toString()
                val seniority = txtExperienceYear.text.toString().toIntOrNull()

                if (company.isEmpty() || seniority == null || selectedSector == null) {
                    val errorMessage = resources.getString(R.string.error_missing_occupation_fields)
                    val sectionName = resources.getString(R.string.main_specific_workers_title)
                    showError(errorMessage.format(sectionName))
                    return
                }

                if (seniority < 0) {
                    val errorMessage = resources.getString(R.string.error_negative_field)
                    val fieldName = resources.getString(R.string.main_specific_experience_title)
                    showError(errorMessage.format(fieldName))
                    return
                }

                person = Worker(
                    name,
                    firstName,
                    toCalendar(birthday),
                    selectedNationality!!,
                    company,
                    selectedSector!!,
                    seniority,
                    email,
                    remarks
                )
            }
            else -> {
                showError(resources.getString(R.string.error_undefined_occupation))
                return
            }
        }
        Log.println(Log.INFO, "Person", person.toString())
    }

    private fun resetFields() {
        txtLastName.text.clear()
        txtFirstName.text.clear()
        txtEmail.text.clear()
        txtRemark.text.clear()
        txtUniversity.text.clear()
        txtGraduationYear.text.clear()
        txtCompany.text.clear()
        txtExperienceYear.text.clear()
        spnNationality.setSelection(0)
        spnSector.setSelection(0)
        radGroup.clearCheck()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun loadPerson(person: Person) {
        txtLastName.setText(person.name)
        txtFirstName.setText(person.firstName)

        val formattedDate = Person.dateFormatter.format(person.birthDay.time)
        txtBirthday.setText(formattedDate)

        val nationalityPosition = nationalityAdapter.getPosition(person.nationality)
        spnNationality.setSelection(nationalityPosition)

        txtEmail.setText(person.email)
        txtRemark.setText(person.remark)

        when (person) {
            is Student -> {
                radStudent.isChecked = true
                txtUniversity.setText(person.university)
                txtGraduationYear.setText(person.graduationYear.toString())
            }
            is Worker -> {
                radWorker.isChecked = true
                txtCompany.setText(person.company)

                val sectorPosition = sectorAdapter.getPosition(person.sector)
                spnSector.setSelection(sectorPosition)

                txtExperienceYear.setText(person.experienceYear.toString())
            }
        }
    }
}