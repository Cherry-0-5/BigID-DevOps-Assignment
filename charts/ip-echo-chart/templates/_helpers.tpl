{{/*
Expand the name of the chart.
*/}}
{{- define "ip-echo-chart.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
*/}}
{{- define "ip-echo-chart.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Common labels (Shared by both Frontend and Backend)
*/}}
{{- define "ip-echo-chart.labels" -}}
helm.sh/chart: {{ printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
{{- end }}

{{/*
Backend Specific Labels (Selector labels for Java App)
*/}}
{{- define "ip-echo-chart.backendLabels" -}}
app: {{ .Values.backend.name }}
component: backend
{{- end }}

{{/*
Frontend Specific Labels (Selector labels for Node.js App)
*/}}
{{- define "ip-echo-chart.frontendLabels" -}}
app: {{ .Values.frontend.name }}
component: frontend
{{- end }}
