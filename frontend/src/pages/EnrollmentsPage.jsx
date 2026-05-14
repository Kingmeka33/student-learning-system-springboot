import { useEffect, useState } from "react";
import { api, getErrorMessage } from "../api/api";

export default function EnrollmentsPage({ currentUser, showToast }) {
  const [students, setStudents] = useState([]);
  const [courses, setCourses] = useState([]);
  const [selectedStudentId, setSelectedStudentId] = useState("");
  const [selectedCourseIds, setSelectedCourseIds] = useState([]);
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  const isAdmin = currentUser?.role === "ADMIN";

  const loadData = async () => {
    try {
      setLoading(true);
      setError("");

      const [studentsResponse, coursesResponse] = await Promise.all([
        api.get("/students"),
        api.get("/courses"),
      ]);

      setStudents(studentsResponse.data);
      setCourses(coursesResponse.data);

      if (isAdmin && studentsResponse.data.length > 0) {
        setSelectedStudentId((currentId) => currentId || studentsResponse.data[0].id);
      }
    } catch (error) {
      setError(getErrorMessage(error, "Unable to load enrollment data."));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const selectedStudent = isAdmin
    ? students.find((student) => student.id === selectedStudentId)
    : students.find((student) => student.email === currentUser?.email);

  const getCourseNames = (courseIds) => {
    return courseIds
      .map((courseId) => courses.find((course) => course.id === courseId)?.title)
      .filter(Boolean);
  };

  const splitDuplicateAndNewCourses = () => {
    const enrolledCourseIds =
      selectedStudent?.courses?.map((course) => course.id) ?? [];

    const duplicateCourseIds = selectedCourseIds.filter((courseId) =>
      enrolledCourseIds.includes(courseId)
    );

    const newCourseIds = selectedCourseIds.filter(
      (courseId) => !enrolledCourseIds.includes(courseId)
    );

    return { duplicateCourseIds, newCourseIds };
  };

  const toggleCourseSelection = (courseId) => {
    setSelectedCourseIds((currentIds) => {
      if (currentIds.includes(courseId)) {
        return currentIds.filter((id) => id !== courseId);
      }

      return [...currentIds, courseId];
    });
  };

  const clearSelectedCourses = () => {
    setSelectedCourseIds([]);
  };

  const handleEnroll = async (event) => {
    event.preventDefault();

    if (selectedCourseIds.length === 0) {
      setError("Please select at least one course.");
      return;
    }

    if (isAdmin && !selectedStudentId) {
      setError("Please select a student.");
      return;
    }

    if (!isAdmin && !selectedStudent) {
      const message =
        "Your login account is not linked to a student record. Ask an admin to create a student with your email address.";
      setError(message);
      showToast?.(message, "error");
      return;
    }

    const { duplicateCourseIds, newCourseIds } = splitDuplicateAndNewCourses();

    if (newCourseIds.length === 0) {
      const duplicateNames = getCourseNames(duplicateCourseIds).join(", ");
      const message = `You are already enrolled in: ${duplicateNames}. Please select a new course.`;
      setError(message);
      showToast?.(message, "error");
      return;
    }

    try {
      setSaving(true);
      setError("");

      if (isAdmin) {
        await api.post("/enrollments/multi", {
          studentId: selectedStudentId,
          courseIds: newCourseIds,
        });
      } else {
        await api.post("/enrollments/me", {
          courseIds: newCourseIds,
        });
      }

      const newNames = getCourseNames(newCourseIds).join(", ");
      const duplicateNames = getCourseNames(duplicateCourseIds).join(", ");

      if (duplicateCourseIds.length > 0) {
        const message = `You cannot enroll in the same courses again: ${duplicateNames}. ${newNames} added successfully.`;
        setError("");
        showToast?.(message, "info");
      } else {
        showToast?.(`${newNames} added successfully.`, "success");
      }

      setSelectedCourseIds([]);
      await loadData();
    } catch (error) {
      const message = getErrorMessage(error, "Unable to complete enrollment.");
      setError(message);
      showToast?.(message, "error");
    } finally {
      setSaving(false);
    }
  };

  const visibleEnrollmentCards = isAdmin
    ? students
    : students.filter((student) => student.email === currentUser?.email);

  return (
    <section>
      <div className="card">
        <h2>Enrollments</h2>

        <p className="muted">
          {isAdmin
            ? "Select a student, then enroll them into one or more available courses."
            : "Select one or more courses below to enroll yourself."}
        </p>

        {loading && <div className="loading">Loading enrollments...</div>}
        {error && <div className="error">{error}</div>}

        <form onSubmit={handleEnroll} className="enrollment-form">
          {isAdmin ? (
            <div>
              <label className="field-label">Student</label>
              <select
                value={selectedStudentId}
                onChange={(event) => {
                  setSelectedStudentId(event.target.value);
                  setSelectedCourseIds([]);
                  setError("");
                }}
              >
                {students.map((student) => (
                  <option key={student.id} value={student.id}>
                    {student.name}
                  </option>
                ))}
              </select>
            </div>
          ) : (
            <div className="student-self-card">
              <strong>{currentUser?.name}</strong>
              <span className="muted">You are enrolling yourself.</span>
            </div>
          )}

          <div>
            <div className="multi-select-header">
              <label className="field-label">Available Courses</label>

              {selectedCourseIds.length > 0 && (
                <button
                  type="button"
                  className="btn-secondary"
                  onClick={clearSelectedCourses}
                >
                  Clear selection
                </button>
              )}
            </div>

            <div className="course-multi-selector">
              {courses.length === 0 ? (
                <p className="muted">No courses are available yet.</p>
              ) : (
                courses.map((course) => {
                  const isSelected = selectedCourseIds.includes(course.id);
                  const isAlreadyEnrolled = selectedStudent?.courses?.some(
                    (enrolledCourse) => enrolledCourse.id === course.id
                  );

                  return (
                    <label
                      key={course.id}
                      className={
                        isSelected
                          ? "course-option selected"
                          : "course-option"
                      }
                    >
                      <input
                        type="checkbox"
                        checked={isSelected}
                        onChange={() => toggleCourseSelection(course.id)}
                      />

                      <span>
                        <strong>{course.title}</strong>
                        <small>{course.code}</small>

                        {isAlreadyEnrolled && (
                          <small className="already-enrolled">
                            Already enrolled
                          </small>
                        )}
                      </span>
                    </label>
                  );
                })
              )}
            </div>

            <p className="muted">Selected courses: {selectedCourseIds.length}</p>
          </div>

          <button type="submit" disabled={saving || selectedCourseIds.length === 0}>
            {saving ? "Enrolling..." : "Enroll for Selected Courses"}
          </button>
        </form>
      </div>

      <div className="list">
        {visibleEnrollmentCards.map((student) => (
          <article key={student.id} className="item-card">
            <h3>{student.name}</h3>
            <p>{student.email}</p>

            <p>
              <strong>Courses:</strong>{" "}
              {student.courses?.length
                ? student.courses.map((course) => (
                    <span className="badge" key={course.id}>
                      {course.title}
                    </span>
                  ))
                : "No courses enrolled yet."}
            </p>
          </article>
        ))}
      </div>
    </section>
  );
}
